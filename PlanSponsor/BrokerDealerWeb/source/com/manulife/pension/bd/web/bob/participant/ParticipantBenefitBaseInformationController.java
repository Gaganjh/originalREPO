package com.manulife.pension.bd.web.bob.participant;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
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

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.BDPdfConstants;
import com.manulife.pension.bd.web.bob.BobContext;
import com.manulife.pension.bd.web.bob.BobContextUtils;
import com.manulife.pension.bd.web.bob.blockOfBusiness.BlockOfBusinessForm;
import com.manulife.pension.bd.web.content.BDContentConstants;
import com.manulife.pension.bd.web.navigation.URLConstants;
import com.manulife.pension.bd.web.report.BDReportController;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.LayoutPage;
import com.manulife.pension.delegate.AccountServiceDelegate;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.EmployeeServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerForward;
import com.manulife.pension.platform.web.delegate.ParticipantServiceDelegate;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.util.ContentHelper;
import com.manulife.pension.platform.web.util.LIADisplayHelper;
import com.manulife.pension.platform.web.util.PDFDocument;
import com.manulife.pension.platform.web.util.PdfHelper;
import com.manulife.pension.ps.service.delegate.AccountServiceDelegateAdaptor;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantBenefitBaseDetails;
import com.manulife.pension.service.account.valueobject.BenefitBaseBatchStatus;
import com.manulife.pension.service.account.valueobject.GiflMarketValue;
import com.manulife.pension.service.account.valueobject.ParticipantGiflData;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.LifeIncomeAmountDetailsVO;
import com.manulife.pension.service.employee.valueobject.Employee;
import com.manulife.pension.service.employee.valueobject.EmployeeDetailVO;
import com.manulife.pension.service.employee.valueobject.TradeRestriction;
import com.manulife.pension.service.report.participant.transaction.valueobject.ParticipantBenefitBaseInformationDataItem;
import com.manulife.pension.service.report.participant.transaction.valueobject.ParticipantBenefitBaseInformationReportData;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.pension.util.content.manager.ContentCacheManager;
import com.manulife.pension.util.log.LogUtility;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.NumberRender;
import com.manulife.util.render.RenderConstants;
import com.manulife.util.render.SSNRender;

/**
 * This is the action class for the NBDW version of the Benefit Base Page
 * 
 * @author Ilamparithi
 *
 */
@Controller
@RequestMapping(value ="/bob")
@SessionAttributes({"participantBenefitBaseInformationForm"})

public class ParticipantBenefitBaseInformationController extends BDReportController {
	@ModelAttribute("participantBenefitBaseInformationForm")
	public ParticipantBenefitBaseInformationForm populateForm() 
	{
		return new ParticipantBenefitBaseInformationForm();
		}
	public static HashMap<String,String>forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/participant/participantBenefitBaseInformation.jsp");
		forwards.put("default","/participant/participantBenefitBaseInformation.jsp");
		forwards.put("filter","/participant/participantBenefitBaseInformation.jsp");
		forwards.put("sort","/participant/participantBenefitBaseInformation.jsp");
		forwards.put("page","/participant/participantBenefitBaseInformation.jsp");
		forwards.put("print","/participant/participantBenefitBaseInformation.jsp");
		}

    protected static EmployeeServiceDelegate employeeServiceDelegate = EmployeeServiceDelegate
            .getInstance(BDConstants.BD_APPLICATION_ID);

    private static Logger logger = Logger
            .getLogger(ParticipantBenefitBaseInformationController.class);

    private static SimpleDateFormat sdf = new SimpleDateFormat(
            ParticipantBenefitBaseInformationForm.FORMAT_DATE_SHORT_MDY);
    
    private static final String[] DOWNLOAD_COLUMN_HEADINGS = new String[] {
            "Transaction Effective Date", "Associated Transaction Number",
            "Transaction Type", "Market Value Before Transaction($)",
            "Transaction Amount($)", "Benefit Base Change($)",
            "Resulting Benefit Base ($)", "MHP Reset" };

    private static final String AMOUNT_FORMAT = "##0.00";

    private static final String CSV_HEADER_FROM_DATE = "From date";

    private static final String CSV_HEADER_TO_DATE = "To date";

    public static final String FORMAT_DATE_SHORT_MDY = "MM/dd/yyyy";

    public static final String ALL_TYPES = "ALL";

    public static final String ZERO_DOLLAR = "0.00";

    public static final String CHECK_ZERO = "0";

    public static final String CHECK_ONE = "1";

    public static final String ZERO = "0";

    public static final String YES = "YES";

    public static final int DECIMAL_DIGITS_TWO = 2;
    
    private static final String PROFILE_ID = "profileId";
    
    private static final String XSLT_FILE_KEY_NAME = "PptBenefitBaseInformationReport.XSLFile";
    
    private static final String CSV_PARTIAL_FILE_NAME = "GuaranteedIncomeFeatureDetails";
    
    private static final String DOWNLOAD_ERROR_FLAG = "downloadErrorFlag";

    static {
        sdf.setLenient(false);
    }
    
    //synchronized method to avoid race condition. 
    protected static synchronized Date  DateParser(String value) throws ParseException{ 
        return sdf.parse(value); 
    }

    /**
     * Constructor.
     */
    public ParticipantBenefitBaseInformationController() {
        super(ParticipantBenefitBaseInformationController.class);
    }

    /**
     * This is an overridden method. This method handles the book marking scenarios.
     * Profile id and participant gifl indicator will not be set in the bob context if the user
     * is using the book mark. In that case we have to manually set those values in the bob context.
     * Since we don't have any reliable way to identify whether the user is coming from book mark, this
     * logic will be executed in all scenarios.
     */
    @Override
    protected String preExecute( ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> preExecute");
        }
        BobContext bobContext = getBobContext(request);
        if (bobContext == null || bobContext.getCurrentContract() == null
                || !bobContext.getCurrentContract().getHasContractGatewayInd()) {
             
            		ControllerForward f =new ControllerForward(URLConstants.HomeURL, false);
            		return f.getPath();
        }
        
        //Only default task can be done through bookmark
        if (getTask(request).equals(DEFAULT_TASK)) {
	       int contractNumber = bobContext.getCurrentContract()
					.getContractNumber();
			String profileId = request.getParameter(PROFILE_ID);
			if (profileId == null) {
				throw new SystemException(
						"preExecute -> Profile Id is not present in the request.");
			}
			//Setting profile Id in bob context in case of Irregular Navigation
			bobContext.setPptProfileId(profileId);
			int participantId = ParticipantServiceDelegate.getInstance()
					.getParticipantIdByProfileId(Long.valueOf(profileId),
							contractNumber);
	
			String participantGiflInd = ParticipantServiceDelegate.getInstance()
					.getParticipantGIFLStatus(String.valueOf(participantId),
							String.valueOf(contractNumber));
	
			// In a normal flow, this will be set when the user chooses a
			// participant from Participant Summary. We have to set it
			// manually for Irregular Navigation
			BobContextUtils.setParticipantGiflInd(participantGiflInd, request);
	
			ParticipantGiflData participantGiflData = AccountServiceDelegate
					.getInstance().getParticipantGiflDetails(
							String.valueOf(contractNumber),
							String.valueOf(participantId));
			if (participantGiflData == null
					|| participantGiflData.getGiflSelectionDate().before(
							bobContext.getCurrentContract()
									.getContractGiflSelectionDate())) {
				//Resetting values in bob context
				Integer nullContractNumber = null;
				bobContext.setCurrentContract(request, nullContractNumber);
				bobContext.setPptProfileId(null);
				bobContext.setParticipantGiflInd(null);
				
						ControllerForward b =new ControllerForward(URLConstants.HomeURL, false);
						return b.getPath();
			}
        }
        return super.preExecute( form, request, response);
    }
    
    @RequestMapping(value ="/participant/participantBenefitBaseInformation/", method =  {RequestMethod.GET}) 
    public String doDefault (@Valid @ModelAttribute("participantBenefitBaseInformationForm") ParticipantBenefitBaseInformationForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	   String forwardPreExecute= preExecute(form, request, response);
           if (forwardPreExecute != null) {
        	  return StringUtils.contains(forwardPreExecute, '/') ? forwardPreExecute : forwards.get(forwardPreExecute);
           }
    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              request.setAttribute(BDConstants.BENEFIT_DETAILS,
	                        new ParticipantBenefitBaseDetails());
	                request.setAttribute(BDConstants.ACCOUNT_DETAILS,
	                        new WebParticipantGiflData());
	                request.setAttribute(BDConstants.LIA_DETAILS,
	    					new LifeIncomeAmountDetailsVO());
	                request.setAttribute(BDConstants.TECHNICAL_DIFFICULTIES, YES);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doDefault() in BlockOfBusinessAction.");
        }
     
        
        Collection<GenericException> errors = doValidate(form,request);
		if (!errors.isEmpty()) {
			BDSessionHelper.setErrorsInSession(request, errors);
			 return forwards.get("input");
        
		}
        
        String forward = doDefault( form, request, response);
        return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
    }  	
    
    
    
    
    /**
     * Validate the input form.The search field must not be empty. To and From
     * dates must be valid format. FROM date must be less than TO date.
     * 
     * @param mapping
     *            ActionMapping
     * @param form
     *            Form
     * @param request
     *            HttpServletRequest
     * @return Collection
     */
 
	@Autowired
	private BDValidatorFWInput bdValidatorFWInput;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(bdValidatorFWInput);
	}
    
    protected Collection doValidate( ActionForm form,
            HttpServletRequest request) {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doValidate()");
        }
        
    

        // Do not validate when first time coming through Default From/To dates
        // are set in populateReportForm() after the doValidate()

        Collection<GenericException> errors = super.doValidate( form,
                request);

        ArrayList<GenericException> transactionErrors = new ArrayList<GenericException>();

        ParticipantBenefitBaseInformationForm participantBenefitBaseInformationForm = (ParticipantBenefitBaseInformationForm) form;

        // Get the task for From date and To date in Transaction history
        if (getTask(request).equals(FILTER_TASK)) {
            transactionErrors = (ArrayList<GenericException>) validateTransactionHistory(
                    participantBenefitBaseInformationForm, request);
        }

        errors.addAll(transactionErrors);

        try {
            if (request
                    .getParameter(ParticipantBenefitBaseInformationForm.PARAMETER_KEY_PROFILE_ID) != null) {
                participantBenefitBaseInformationForm
                        .setProfileId((String) request
                                .getParameter(ParticipantBenefitBaseInformationForm.PARAMETER_KEY_PROFILE_ID));
            }
            retrieveParticipantDetails(participantBenefitBaseInformationForm
                    .getProfileId(), request);
            getParticipantBenefitBaseAccountDetails(
                    (BaseReportForm) form, request);
            
            getParticipantLIADetails(participantBenefitBaseInformationForm
					.getProfileId(), request);
        } catch (SystemException se) {
            request.setAttribute(BDConstants.BENEFIT_DETAILS,
                    new ParticipantBenefitBaseDetails());
            request.setAttribute(BDConstants.ACCOUNT_DETAILS,
                    new WebParticipantGiflData());
            request.setAttribute(BDConstants.LIA_DETAILS,
					new LifeIncomeAmountDetailsVO());

            // Log the system exception.
            LogUtility.logSystemException(BDConstants.BD_APPLICATION_ID, se);
            
            // Show user friendly message.
            errors.clear();
            errors.add(new GenericException(BDErrorCodes.TECHNICAL_DIFFICULTIES));
            request.setAttribute(BDConstants.TECHNICAL_DIFFICULTIES, YES);
        }
        return errors;
    }
    

    /**
     * Validate the From date and the To Date in the Transaction History Section
     * 
     * @param participantBenefitBaseInformationForm
     *            ParticipantBenefitBaseInformationForm
     * @param request
     *            HttpServletRequest
     * @param mapping
     *            ActionMapping
     * @return Collection<GenericException>
     */
    private Collection<GenericException> validateTransactionHistory(
            ParticipantBenefitBaseInformationForm participantBenefitBaseInformationForm,
            HttpServletRequest request) {

        ArrayList<GenericException> errors = new ArrayList<GenericException>();

        Date fromDate = new Date();
        Date toDate = new Date();
        boolean validDates = false;
        boolean validFromDate = false;
        boolean validFromDateRange = false;
        Date currentDate = getMax24MonthsCutOffDate();

        // FROM date empty
        if ((StringUtils.isEmpty(participantBenefitBaseInformationForm
                .getFromDate()))
                && (!StringUtils.isEmpty(participantBenefitBaseInformationForm
                        .getToDate()))) {

            errors.add(new GenericException(BDErrorCodes.FROM_DATE_EMPTY));
        }

        if ((StringUtils.isEmpty(participantBenefitBaseInformationForm
                .getToDate()))
                && (!StringUtils.isEmpty(participantBenefitBaseInformationForm
                        .getFromDate()))) {

            errors.add(new GenericException(BDErrorCodes.TO_DATE_EMPTY));
        }

        // Both dates empty
        if ((StringUtils.isEmpty(participantBenefitBaseInformationForm
                .getFromDate()))
                && (StringUtils.isEmpty(participantBenefitBaseInformationForm
                        .getToDate()))) {

            errors.add(new GenericException(BDErrorCodes.BOTH_DATES_EMPTY));
        }

        // Valid date format
        if ((participantBenefitBaseInformationForm.getToDate().trim().length() > 0)
                && (participantBenefitBaseInformationForm.getFromDate().trim()
                        .length() > 0)) {

            try {
                fromDate = validateDateFormat(participantBenefitBaseInformationForm
                        .getFromDate());
                toDate = validateDateFormat(participantBenefitBaseInformationForm
                        .getToDate());
                validDates = true;
                validFromDate = true;
            } catch (Exception e) {
                errors.add(new GenericException(BDErrorCodes.INVALID_DATE));
                validDates = false;
                validFromDate = false;
            }
        }

        // Empty FROM date, invalid TO date
        if ((StringUtils.isEmpty(participantBenefitBaseInformationForm
                .getFromDate()))
                && (participantBenefitBaseInformationForm.getToDate().trim()
                        .length() > 0)) {

            try {
                toDate = validateDateFormat(participantBenefitBaseInformationForm
                        .getToDate());
                validDates = true;
            } catch (Exception e) {
                errors.add(new GenericException(BDErrorCodes.INVALID_DATE));
                validDates = false;
            }
        }

        // Invalid FROM date format, empty TO date
        if ((participantBenefitBaseInformationForm.getFromDate().trim()
                .length() > 0)
                && (participantBenefitBaseInformationForm.getToDate().trim()
                        .length() == 0)) {

            try {
                fromDate = validateDateFormat(participantBenefitBaseInformationForm
                        .getFromDate());
                validFromDate = true;

            } catch (Exception e) {
                errors.add(new GenericException(BDErrorCodes.INVALID_DATE));
                validFromDate = false;
            }
        }

        // Valid FROM date, empty TO date, and FROM date greater than
        // default TO date
        if (validFromDate
                && (!StringUtils.isEmpty(participantBenefitBaseInformationForm
                        .getFromDate()))
                && (StringUtils.isEmpty(participantBenefitBaseInformationForm
                        .getToDate()))) {

            if (fromDate.after(toDate)) {
                errors
                        .add(new GenericException(
                                BDErrorCodes.FROM_DATE_AFTER_TO));
                validFromDateRange = false;
            } else {
                validFromDateRange = true;
            }
        }

        // Valid FROM date, empty TO date, and FROM date not within the
        // last 24 months of default TO date
        if (validFromDate
                && (!StringUtils.isEmpty(participantBenefitBaseInformationForm
                        .getFromDate()))
                && (StringUtils.isEmpty(participantBenefitBaseInformationForm
                        .getToDate()))) {
            if (fromDate.before(currentDate)) {
                errors.add(new GenericException(
                        BDErrorCodes.FROM_DATE_BEFORE_24_MONTHS));
                validFromDateRange = false;
            } else {
                validFromDateRange = true;
            }
        }

        // If from date valid, date range valid, and to date is empty,
        // then set default TO Date
        if (validFromDateRange
                && (!StringUtils.isEmpty(participantBenefitBaseInformationForm
                        .getFromDate()))
                && (StringUtils.isEmpty(participantBenefitBaseInformationForm
                        .getToDate()))) {
            Calendar cal = Calendar.getInstance();
            // cal.setTime(asOfDate);
            participantBenefitBaseInformationForm.setToDate(DateRender
                    .formatByPattern(cal.getTime(), "",
                            RenderConstants.MEDIUM_MDY_SLASHED));
        }

        // From date must be earlier than To date
        if (validDates
                && ((!StringUtils.isEmpty(participantBenefitBaseInformationForm
                        .getFromDate())) && (!StringUtils
                        .isEmpty(participantBenefitBaseInformationForm
                                .getToDate()))) && (fromDate.after(toDate))) {
            errors.add(new GenericException(BDErrorCodes.FROM_DATE_AFTER_TO));
        }

        // From date outside 24 month range
        if (validDates
                && ((!StringUtils.isEmpty(participantBenefitBaseInformationForm
                        .getFromDate())) && (!StringUtils
                        .isEmpty(participantBenefitBaseInformationForm
                                .getToDate())))
                && (fromDate.before(currentDate))) {
            if (request
                    .getParameter(ParticipantBenefitBaseInformationForm.PARAMETER_KEY_PARTICIPANT_ID) != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(currentDate);
                participantBenefitBaseInformationForm.setFromDate(DateRender
                        .formatByPattern(cal.getTime(), "",
                                RenderConstants.MEDIUM_MDY_SLASHED));
            } else {
                errors.add(new GenericException(
                        BDErrorCodes.FROM_DATE_BEFORE_24_MONTHS));
            }
        }

        return errors;
    }

    /**
     * This method is used for validating Date
     * 
     * @param dateString
     *            String
     * @return Date
     * @throws ParseException
     */
    private Date validateDateFormat(String dateString) throws ParseException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> validateDateFormat");
        }
        Date validDate = null;
        if (!((dateString.trim().length() == 10)
                && (dateString.substring(2, 3).equals("/")) && (dateString
                .substring(5, 6)).equals("/"))) {
            throw new ParseException("invalid date format", 0);
        }
        String month = dateString.substring(0, 2);
        String day = dateString.substring(3, 5);
        String year = dateString.substring(6, 10);
        try {
            Integer.parseInt(year);
            if (Integer.parseInt(month) < 1 || Integer.parseInt(month) > 12)
                throw new ParseException("invalid month", 0);

            if (Integer.parseInt(day) < 1 || Integer.parseInt(day) > 31)
                throw new ParseException("invalid day", 0);

            if (Integer.parseInt(day) == 29 && (Integer.parseInt(month) == 2)
                    && (Integer.parseInt(year) % 4 > 0))
                throw new ParseException("invalid day", 0);

        } catch (Exception e) {
            throw new ParseException("invalid date format", 0);
        }

        validDate = DateParser(dateString);

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- validateDateFormat");
        }
        return validDate;
    }

    /**
     * This is used for the validation of 24 months period
     * 
     * @return Date
     */
    private Date getMax24MonthsCutOffDate() {

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -2);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * This is method is used for specifying the default sort criteria
     * 
     * @return String
     */
    protected String getDefaultSort() {
        return ParticipantBenefitBaseInformationReportData.SORT_FIELD_EFFECTIVE_DATE;
    }

    /**
     * This is method is used for specifying the default sort direction
     * 
     * @return String
     */
    protected String getDefaultSortDirection() {
        return ReportSort.DESC_DIRECTION;
    }
    
    /*
	 * If there are errors in the search criteria while doing the download we
	 * are not supposed to retrieve the transactions. So we make the report bean
	 * as null instead of retrieving the report data.
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.manulife.pension.bd.web.report.BDReportAction#doCommon(org.apache
	 * .struts.action.ActionMapping,
	 * com.manulife.pension.platform.web.report.BaseReportForm,
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
    @Override
    protected String doCommon(
			BaseReportForm reportForm, HttpServletRequest request,
			HttpServletResponse response) throws SystemException {
		if (getTask(request).equals(DOWNLOAD_TASK)
				&& BDConstants.YES.equals(request
						.getAttribute(DOWNLOAD_ERROR_FLAG))) {
			request.setAttribute(BDConstants.REPORT_BEAN, null);
			return forwards.get( DOWNLOAD_TASK);
		} else {
			return super.doCommon( reportForm, request, response);
		}
	}
    
    /*
	 * This method is overridden to set a flag to indicate whether errors are
	 * present. Based on that we will decide whether to retrieve the
	 * transactions or not.
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.manulife.pension.platform.web.report.BaseReportAction#doDownload(
	 * org.apache.struts.action.ActionMapping,
	 * com.manulife.pension.platform.web.report.BaseReportForm,
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
    @RequestMapping(value ="/participant/participantBenefitBaseInformation/", params={"task=download"} , method =  {RequestMethod.GET,RequestMethod.POST}) 
    public String doDownload(@Valid @ModelAttribute("participantBenefitBaseInformationForm") ParticipantBenefitBaseInformationForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	  String forwardPreExecute= preExecute(actionForm, request, response);
          if (forwardPreExecute != null) {
       	  return StringUtils.contains(forwardPreExecute, '/') ? forwardPreExecute : forwards.get(forwardPreExecute);
          }
    	if(bindingResult.hasErrors()){
    		request.setAttribute(BDConstants.BENEFIT_DETAILS,
                    new ParticipantBenefitBaseDetails());
            request.setAttribute(BDConstants.ACCOUNT_DETAILS,
                    new WebParticipantGiflData());
            request.setAttribute(BDConstants.LIA_DETAILS,
					new LifeIncomeAmountDetailsVO());
            request.setAttribute(BDConstants.TECHNICAL_DIFFICULTIES, YES);
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
        
        Collection<GenericException> errorsValidate = doValidate(actionForm,request);
		if (!errorsValidate.isEmpty()) {
			BDSessionHelper.setErrorsInSession(request, errorsValidate);
			 return forwards.get("input");
        
		}
    	Collection<GenericException> errors = validateTransactionHistory(
				(ParticipantBenefitBaseInformationForm) actionForm, request
				);
    	if(!errors.isEmpty()) {
    		request.setAttribute(DOWNLOAD_ERROR_FLAG, BDConstants.YES);
    	}
    	return super.doDownload( actionForm, request, response);
    }

    /**
     * This method is used for getting the data for download csv file
     * 
     * @param reportForm
     * @parm report
     * @parm request
     * @return byte[]
     * @throws SystemException
     */
    @SuppressWarnings("unchecked")
    protected byte[] getDownloadData(BaseReportForm reportForm,
            ReportData report, HttpServletRequest request)
            throws SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getDownloadData");
        }

        ParticipantBenefitBaseInformationReportData data = (ParticipantBenefitBaseInformationReportData) report;
        StringBuffer buffer = new StringBuffer();

        ParticipantBenefitBaseInformationForm theForm = (ParticipantBenefitBaseInformationForm) reportForm;

        BobContext bobContext = getBobContext(request);
        Contract currentContract = bobContext.getCurrentContract();
        Date fromDate = new Date();
        Date toDate = new Date();
        Date asOfDate = theForm.getAsOfDate();
        SimpleDateFormat format = new SimpleDateFormat(FORMAT_DATE_SHORT_MDY);

        ParticipantBenefitBaseDetails participantBenefitBaseDetails = (ParticipantBenefitBaseDetails) request
                .getAttribute(BDConstants.BENEFIT_DETAILS);

        ParticipantGiflData participantGiflData = (ParticipantGiflData) request
                .getAttribute(BDConstants.ACCOUNT_DETAILS);

        // To display Trade Restriction Date in CSV file for GIFL version 3
        TradeRestriction tradeRestriction = (TradeRestriction) request
                .getAttribute(BDConstants.TRADING_RESTRICTION);

        String strDeselectionDate = DateRender.formatByPattern(
                participantGiflData.getGiflDeselectionDate(), "",
                RenderConstants.MEDIUM_YMD_DASHED);

        String strActivationDate = DateRender.formatByPattern(
                participantGiflData.getGiflActivationDate(), "",
                RenderConstants.MEDIUM_YMD_DASHED);

        String strLastStepUpDate = DateRender.formatByPattern(
                participantGiflData.getGiflLastStepUpDate(), "",
                RenderConstants.MEDIUM_YMD_DASHED);

        String strTradingRestricationDate = DateRender.formatByPattern(
                tradeRestriction.getTradeRestrictionEndDate(), "",
                RenderConstants.MEDIUM_YMD_DASHED);
        String giflVersion = currentContract.getGiflVersion();
        String showTradingExpirationDate = BDConstants.NO;

        // Trading Expiration date will only be displayed if the this date is in
        // effect and it should be after GIFL selection activation date
        if (tradeRestriction != null
                && tradeRestriction.isTradeRestrictionInEffect()
                && tradeRestriction.getTradeRestrictionEndDate().after(
                        participantGiflData.getGiflSelectionDate())) {
            showTradingExpirationDate = BDConstants.YES;
        }

        // GIFL 2A : Benefit base batch out of date warning message
        if (BDConstants.YES.equals(theForm.getBbBatchDateLessThenETL())) {
            try {
                Content message = null;
                message = ContentCacheManager
                        .getInstance()
                        .getContentById(
                                BDContentConstants.MISCELLANEOUS_BENEFIT_BASE_BATCH_OUT_OF_DATE,
                                ContentTypeManager.instance().MESSAGE);

                buffer.append(
                        ContentUtility.getContentAttribute(message, "text"))
                        .append(LINE_BREAK);

            } catch (ContentException exp) {
                throw new SystemException(exp,
                        "Exception in getDownloadData. Something wrong with CMA");
            }
		} 
        // GIFL LIA : Lifetime Income Amount selected warning message
        if (theForm.isShowLIADetailsSection()) {
			try {
				String message = getContentMessageByCmaKey(BDContentConstants.MISCELLANEOUS_BENEFIT_BASE_LIA_MESSAGE);
				buffer.append(QUOTE).append(message).append(QUOTE).append(LINE_BREAK);
			} catch (ContentException exception) {
				throw new SystemException(exception,
						"Exception in getDownloadData. Something wrong with Life Income Amount CMA");
			}
		}

        buffer.append("Contract").append(COMMA).append(
                currentContract.getContractNumber()).append(COMMA).append(
                currentContract.getCompanyName()).append(LINE_BREAK);

        // Get dates for display
        buffer.append("As of,").append(
                DateRender.formatByPattern(asOfDate, "",
                        RenderConstants.MEDIUM_MDY_SLASHED)).append(LINE_BREAK)
                .append(LINE_BREAK);

        try {
            fromDate = format.parse(theForm.getFromDate());
        } catch (ParseException parseException) {
            if (logger.isDebugEnabled()) {
                logger.debug("ParseException in fromDate getDownloadData()"
                        + " ParticipantBenefitBaseInformationAction:",
                        parseException);
            }
        }
        try {
            toDate = format.parse(theForm.getToDate());
        } catch (ParseException parseException) {
            if (logger.isDebugEnabled()) {
                logger.debug("ParseException in fromDate getDownloadData()"
                        + " ParticipantBenefitBaseInformationAction:",
                        parseException);
            }
        }

        try {
            // Retrieving participant summary details
            buffer.append("Last Name,First Name,Middle Initial,SSN,");

            if (participantBenefitBaseDetails.getDateOfBirth() != null) {
                buffer.append("Date of Birth,");
            }
            buffer.append("Benefit Base,");
            buffer.append("Market Value,");

            buffer.append(LINE_BREAK);
            buffer.append(QUOTE).append(
                    participantBenefitBaseDetails.getLastName()).append(QUOTE)
                    .append(COMMA);
            buffer.append(QUOTE).append(
                    participantBenefitBaseDetails.getFirstName()).append(QUOTE)
                    .append(COMMA);
            if (participantBenefitBaseDetails.getMiddleInitial() != null) {
                buffer.append(QUOTE).append(
                        participantBenefitBaseDetails.getMiddleInitial())
                        .append(QUOTE).append(COMMA);
            } else {
                buffer.append(QUOTE).append("").append(QUOTE).append(COMMA);
            }
            buffer.append(
                    SSNRender.format(new String(participantBenefitBaseDetails
                            .getSsn()), null, true)).append(COMMA);
            // Checking for birth date default condition
            if (participantBenefitBaseDetails.getDateOfBirth() != null)
                buffer.append(participantBenefitBaseDetails.getDateOfBirth())
                        .append(COMMA);

            buffer.append(
                    NumberRender.formatByPattern(participantGiflData
                            .getGiflBenefitBaseAmt(), ZERO_AMOUNT_STRING,
                            AMOUNT_FORMAT)).append(COMMA);

            buffer.append(
                    NumberRender.formatByPattern(participantBenefitBaseDetails
                            .getMarketValueGoFunds(), ZERO_AMOUNT_STRING,
                            AMOUNT_FORMAT)).append(COMMA);

            buffer.append(LINE_BREAK).append(LINE_BREAK);

            buffer.append("Selection Date,");

            if (!strDeselectionDate.equals(BDConstants.DEFAULT_DATE)) {
                buffer.append("Deactivation Date, ");
            }
            buffer.append("Activation Date,");
            //This should be hide for valid LIA anniversary date
			if (!theForm.isShowLIADetailsSection()) {
				buffer.append("Anniversary Date,");
			}
            buffer.append("Minimum Holding Period Expiry Date,");

                // //Trading Expiration date will only be displayed if the this
            // date is in effect and it should be after GIFL selection
            // activation date
            if (BDConstants.YES.equals(showTradingExpirationDate)) {
                buffer.append("Trading Restriction Expiry Date,");
            }

            // Added new Fields which should be displayed if the contract has
            // GIFL version 3 features
            if (BDConstants.GIFL_VERSION_03.equals(giflVersion)) {
                buffer.append("Rate for Last Income Enhancement,");
                buffer.append("Last Income Enhancement Date,");
                buffer.append("Value Changed at Last Income Enhancement");
            } else {
                buffer.append("Last Step-Up Date,");
                buffer.append("Value Changed at Last Step-Up Date");
            }
            buffer.append(LINE_BREAK);

            buffer.append(
                    DateRender.formatByPattern(participantGiflData
                            .getGiflSelectionDate(), "",
                            RenderConstants.MEDIUM_MDY_SLASHED)).append(COMMA);

            // Check for end date is default or not
            if (!strDeselectionDate.equals(BDConstants.DEFAULT_DATE))
                buffer.append(
                        DateRender.formatByPattern(participantGiflData
                                .getGiflDeselectionDate(), "",
                                RenderConstants.MEDIUM_MDY_SLASHED)).append(
                        COMMA);

            // Check for active date conditions
            if (!strDeselectionDate.equals(BDConstants.DEFAULT_DATE)) {

                buffer.append(BDConstants.NA).append(COMMA);

            } else if (strDeselectionDate.equals(BDConstants.DEFAULT_DATE)
                    && strActivationDate.equals(BDConstants.DEFAULT_DATE)) {

                buffer.append(BDConstants.AWAITING_DEPOSIT).append(COMMA);

            } else if (strDeselectionDate.equals(BDConstants.DEFAULT_DATE)
                    && !strActivationDate.equals(BDConstants.DEFAULT_DATE)) {

                buffer.append(
                        DateRender.formatByPattern(participantGiflData
                                .getGiflActivationDate(), "",
                                RenderConstants.MEDIUM_MDY_SLASHED)).append(
                        COMMA);
            }
            //This should be hide for valid LIA anniversary date
			if (!theForm.isShowLIADetailsSection()) {
				// To display the anniversary date in date format
				if (!strDeselectionDate.equals(BDConstants.DEFAULT_DATE)) {

					buffer.append(BDConstants.NA).append(COMMA);

				} else if (strDeselectionDate.equals(BDConstants.DEFAULT_DATE)
						&& strActivationDate.equals(BDConstants.DEFAULT_DATE)) {

					buffer.append(BDConstants.AWAITING_DEPOSIT).append(COMMA);

				} else if (strDeselectionDate.equals(BDConstants.DEFAULT_DATE)
						&& !strActivationDate.equals(BDConstants.DEFAULT_DATE)) {

					buffer.append(
							DateRender.formatByPattern(participantGiflData
									.getGiflNextStepUpDate(), "",
									RenderConstants.MEDIUM_MDY_SLASHED))
							.append(COMMA);
				}
			}

            // Check for Holding period expiry date conditions
            if (!strDeselectionDate.equals(BDConstants.DEFAULT_DATE)) {

                buffer.append(BDConstants.NA).append(COMMA);
            } else if (strDeselectionDate.equals(BDConstants.DEFAULT_DATE)
                    && strActivationDate.equals(BDConstants.DEFAULT_DATE)) {

                buffer.append(BDConstants.AWAITING_DEPOSIT).append(COMMA);
            } else if (strDeselectionDate.equals(BDConstants.DEFAULT_DATE)
                    && !strActivationDate.equals(BDConstants.DEFAULT_DATE)) {

                buffer.append(
                        DateRender.formatByPattern(participantGiflData
                                .getGiflHoldingPeriodExpDate(), "",
                                RenderConstants.MEDIUM_MDY_SLASHED)).append(
                        COMMA);
            }

            // Check for Trading Restriction Expiry Date conditions
            if (BDConstants.YES.equals(showTradingExpirationDate)) {
                if (BDConstants.DEFAULT_DATE.equals(strTradingRestricationDate)) {
                    buffer.append(BDConstants.NA).append(COMMA);
                } else {
                    buffer.append(strTradingRestricationDate).append(COMMA);
                }
            }
            if (BDConstants.GIFL_VERSION_03.equals(giflVersion)) {
                // Display Rate at last Income Enhancement Amount
                buffer.append(theForm.getRateForLastIncomeEnhancement())
                        .append(COMMA);
            }

            // Check for Last step up date conditions
            if (BDConstants.DEFAULT_DATE.equals(strLastStepUpDate)) {

                buffer.append(BDConstants.NA).append(COMMA);
            } else if (strDeselectionDate.equals(BDConstants.DEFAULT_DATE)
                    && !strLastStepUpDate.equals(BDConstants.DEFAULT_DATE)) {

                buffer.append(
                        DateRender.formatByPattern(participantGiflData
                                .getGiflLastStepUpDate(), "",
                                RenderConstants.MEDIUM_MDY_SLASHED)).append(
                        COMMA);
            } else { // For the scenario, GIFL is deselected and atleast one
                // step-up had occurred

                buffer.append(
                        DateRender.formatByPattern(participantGiflData
                                .getGiflLastStepUpDate(), "",
                                RenderConstants.MEDIUM_MDY_SLASHED)).append(
                        COMMA);
            }

            // Check for Value changed at Last step up date conditions
            if (BDConstants.DEFAULT_DATE.equals(strLastStepUpDate)) {

                buffer.append(BDConstants.NA).append(COMMA);
            } else if (strDeselectionDate.equals(BDConstants.DEFAULT_DATE)
                    && !strLastStepUpDate.equals(BDConstants.DEFAULT_DATE)) {

                buffer.append(
                        NumberRender.formatByPattern(participantGiflData
                                .getGiflLastStepUpChangeAmt(),
                                ZERO_AMOUNT_STRING, AMOUNT_FORMAT)).append(
                        COMMA);
            } else { // For the scenario, GIFL is deselected and atleast one
                // step-up had occurred

                buffer.append(
                        NumberRender.formatByPattern(participantGiflData
                                .getGiflLastStepUpChangeAmt(),
                                ZERO_AMOUNT_STRING, AMOUNT_FORMAT)).append(
                        COMMA);
            }

            buffer.append(LINE_BREAK).append(LINE_BREAK);

			if (theForm.isShowLIADetailsSection()) {

				// Get dates for display
				try{
					buffer.append(getContentMessageByCmaKey(BDContentConstants.MISCELLANEOUS_LIA_SELECTION_DATE_FIELD_LABEL)).append(COMMA);
					buffer.append(getContentMessageByCmaKey(BDContentConstants.MISCELLANEOUS_LIA_SPOUSAL_OPTION_FIELD_LABEL)).append(COMMA);
					buffer.append(getContentMessageByCmaKey(BDContentConstants.MISCELLANEOUS_LIA_PERCENTAGE_FIELD_LABEL)).append(COMMA);
					buffer.append(getContentMessageByCmaKey(BDContentConstants.MISCELLANEOUS_ANNUAL_LIA_AMOUNT_FIELD_LABEL)).append(COMMA);
					buffer.append(getContentMessageByCmaKey(BDContentConstants.MISCELLANEOUS_LIA_PAYMENT_FREQUENCY_FIELD_LABEL)).append(COMMA);
					buffer.append(getContentMessageByCmaKey(BDContentConstants.MISCELLANEOUS_LIA_ANNIVERSARY_DATE_FIELD_LABEL));
				}  catch (ContentException exception) {
					throw new SystemException(exception.getMessage());
				}
				buffer.append(LINE_BREAK);

				buffer.append(
						DateRender.formatByPattern(
								theForm.getLiaSelectionDate(), "",
								RenderConstants.MEDIUM_MDY_SLASHED)).append(
						COMMA);
				buffer.append(QUOTE)
						.append(theForm
								.getLiaIndividualOrSpousalOption())
						.append(QUOTE).append(COMMA);

				buffer.append(QUOTE)
						.append(theForm.getLiaPercentage())
						.append(QUOTE).append(COMMA);
				if (theForm.getLiaAnnualAmount() != null) {
					buffer.append(
							NumberRender.formatByPattern(
									theForm.getLiaAnnualAmount(),
									ZERO_AMOUNT_STRING, AMOUNT_FORMAT));
				}
				buffer.append(COMMA);
				buffer.append(QUOTE).append(
						theForm.getLiaFrequencyCode()).append(
						QUOTE);

				if (theForm.getLiaPeriodicAmt() != null) {
					buffer.append(" - ").append(
							NumberRender.formatByPattern(
									theForm.getLiaPeriodicAmt(),
									ZERO_AMOUNT_STRING, AMOUNT_FORMAT));
				}
				buffer.append(COMMA);
				buffer.append(
						DateRender.formatByPattern(
								theForm.getLiaAnniversaryDate(), "",
								RenderConstants.MEDIUM_MDY_SLASHED))
						.append(COMMA).append(LINE_BREAK).append(LINE_BREAK);

			}
            String fromDateStr = DateRender.format(fromDate,
                    RenderConstants.MEDIUM_MDY_SLASHED);

            String toDateStr = DateRender.format(toDate,
                    RenderConstants.MEDIUM_MDY_SLASHED);

            buffer.append(CSV_HEADER_FROM_DATE).append(COMMA);
            buffer.append(CSV_HEADER_TO_DATE).append(COMMA);
            buffer.append(LINE_BREAK);
            buffer.append(fromDateStr).append(COMMA);
            buffer.append(toDateStr).append(LINE_BREAK);

            buffer.append(LINE_BREAK);
            if (data == null || data.getDetails().size() == 0) {
            	request.setAttribute(DOWNLOAD_ERROR_FLAG, null);
                Content message = null;
                message = ContentCacheManager
                        .getInstance()
                        .getContentById(
                                BDContentConstants.MESSAGE_NO_HISTORY_TRANSACTION_FOR_DATE_SELECTED,
                                ContentTypeManager.instance().MESSAGE);

                buffer.append(
                        ContentUtility.getContentAttribute(message, "text"))
                        .append(LINE_BREAK);
            } else {
                for (int i = 0; i < DOWNLOAD_COLUMN_HEADINGS.length; i++) {
                    buffer.append(DOWNLOAD_COLUMN_HEADINGS[i]);
                    if (i != DOWNLOAD_COLUMN_HEADINGS.length - 1)
                        buffer.append(COMMA);
                }
	            buffer.append(LINE_BREAK);
	            Iterator iterator = report.getDetails().iterator();
	            while (iterator.hasNext()) {
	                ParticipantBenefitBaseInformationDataItem theItem = (ParticipantBenefitBaseInformationDataItem) iterator
	                        .next();
	                String effectiveTransactionDate = DateRender.format(theItem
	                        .getTransactionEffectiveDate(), null);
	                String associatedTransactionNumber = "";
	                if (!theItem.getTransactionNumber().equals("0"))
	                    associatedTransactionNumber = theItem
	                            .getTransactionNumber().toString();
	                String transactionTypeDesc = theItem.getTransactionType();
	                buffer.append(effectiveTransactionDate).append(COMMA);
	                buffer.append(associatedTransactionNumber).append(COMMA);
	                buffer.append(transactionTypeDesc).append(COMMA);
	
	                // In CSV files, across the applications, amount values are
	                // displayed without zeros after the decimal
	                // point. (Ex. 1200.00 as 1200). The amount strings in the BB
	                // Report are formatted with commas so
	                // they will be displayed with the zeros in csv files. To keep
	                // this report consistent with other
	                // reports in ezk and psw application we have to remove the
	                // commas.
	
	                buffer.append(
	                        getCsvString(removeCommas(theItem
	                                .getMarketValueBeforeTransaction()))).append(
	                        COMMA);
	
	                buffer.append(
	                        getCsvString(removeCommas(theItem
	                                .getTransactionAmount()))).append(COMMA);
	                buffer.append(
	                        getCsvString(removeCommas(theItem
	                                .getBenefitBaseChangeAmount()))).append(COMMA);
	                buffer.append(
	                        getCsvString(removeCommas(NumberRender.formatByType(
	                                theItem.getBenefitBaseAmount(), "0.00", "d",
	                                DECIMAL_DIGITS_TWO, BigDecimal.ROUND_UP, 1))))
	                        .append(COMMA);
	                buffer.append(theItem.getHoldingPeriodInd()).append(COMMA);
	                buffer.append(LINE_BREAK);
	            }
            }
        } catch (ContentException exp) {
            throw new SystemException(exp,
                    " Exception in getDownloadData. Something wrong with CMA");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("exit <- getDownloadData");
        }

        return buffer.toString().getBytes();
    }

    /**
     * This method is used to remove commas in a string.
     * 
     * @param value
     * 
     * @return String a comma less string
     */
    private String removeCommas(String value) {
        if (value != null && value.indexOf(",") != -1) {
            value = value.replaceAll(",", "");
        }
        return value;
    }  

    /**
     * Set sorting criteria
     * 
     * @param criteria
     * @param form
     */
    protected void populateSortCriteria(ReportCriteria criteria,
            BaseReportForm form) {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> populateSortCriteria");
        }

        String sortField = form.getSortField();
        String sortDirection = form.getSortDirection();

        criteria.insertSort(sortField, sortDirection);

        // Sort by descending (ascending) Effective transaction date
        if (sortField
                .equals(ParticipantBenefitBaseInformationReportData.SORT_FIELD_EFFECTIVE_DATE)) {
            criteria
                    .insertSort(
                            ParticipantBenefitBaseInformationReportData.SORT_FIELD_SEQNO,
                            sortDirection);

            criteria
                    .insertSort(
                            ParticipantBenefitBaseInformationReportData.SORT_FIELD_TRANSACTION_NO,
                            sortDirection);

        }
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> populateSortCriteria");
        }
    }

    /**
     * This method is used to sort the table values in reverse direction
     * 
     * @param sortDirection
     *            String
     * @return String
     */
    @SuppressWarnings("unused")
    private String getReversedSort(String sortDirection) {
        return (sortDirection.equalsIgnoreCase(ReportSort.ASC_DIRECTION) ? ReportSort.DESC_DIRECTION
                : ReportSort.ASC_DIRECTION);
    }

    /**
     * To get the Participant's benefit base account details
     * 
     * @param reportForm
     *            BaseReportForm
     * @param request
     *            HttpServletRequest
     * @throws SystemException
     */

    private void getParticipantBenefitBaseAccountDetails(
            BaseReportForm reportForm, HttpServletRequest request)
            throws SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getParticipantBenefitBaseAccountDetails");
        }
        BobContext bobContext = getBobContext(request);
        Contract currentContract = bobContext.getCurrentContract();
        int contractNumber = currentContract.getContractNumber();

        ParticipantBenefitBaseInformationForm theForm = (ParticipantBenefitBaseInformationForm) reportForm;

        int participantIdInt = ParticipantServiceDelegate.getInstance()
                .getParticipantIdByProfileId(
                        Long.parseLong(theForm.getProfileId()), contractNumber);
        theForm.setParticipantId(String.valueOf(participantIdInt));
        theForm.setProposalNumber(currentContract.getProposalNumber());

        ParticipantGiflData participantGiflData = AccountServiceDelegate
                .getInstance().getParticipantGiflDetails(
                        String.valueOf(contractNumber),
                        theForm.getParticipantId());
        if (participantGiflData == null) {
            StringBuffer exceptionMessage = new StringBuffer();
            exceptionMessage.append("Participant belongs to contractId:");
            exceptionMessage.append(contractNumber);
            exceptionMessage.append(" ,profileId:");
            exceptionMessage.append(theForm.getProfileId());
            exceptionMessage.append(" has never selected gateway");

            throw new SystemException(exceptionMessage.toString());
        }
        //To format the dates in a different format for NBDW
        WebParticipantGiflData webGiflData = WebParticipantGiflDataHelper.transformToWebFormat(participantGiflData);
        webGiflData.setParticipantId(new BigDecimal(theForm
                .getParticipantId()));

        // set the default from date
        if (theForm.getFromDate() == null) {
            theForm.setFromDate(getDefaultFromDate(webGiflData
                    .getGiflSelectionDate()));
        }

        // set the gifl selection date as participant's latest GIFL selection date
        theForm.setGiflSelectionDate(
                DateRender.formatByPattern(participantGiflData.getGiflSelectionDate(),
                        "", RenderConstants.MEDIUM_MDY_SLASHED));
        
        // Get the Trade Restriction Expiration Date and whether this date is in
        // effect
        try {
            request.setAttribute(BDConstants.TRADING_RESTRICTION,
                    getTradingRestriction(Long.valueOf(participantIdInt),
                            Integer
                                    .valueOf(currentContract
                                            .getProposalNumber())));
        } catch (Exception e) {
            logger
                    .error("ParticipantBenefitBaseInformationAction -> getParticipantBenefitBaseAccountDetails -> getTradingRestriction");
            throw new SystemException(e.getMessage());
        }

        // Set the GIFL data object in the request
        request.setAttribute(BDConstants.ACCOUNT_DETAILS, webGiflData);

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- participantGiflData");
        }

    }

    /**
     * To populate report action form.
     * 
     * @param mapping
     *            ActionMapping
     * @param reportForm
     *            BaseReportForm
     * @param request
     *            HttpServletRequest
     * @throws SystemException
     */
    protected void populateReportForm(
            BaseReportForm reportForm, HttpServletRequest request) {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> populateReportForm");
        }
        super.populateReportForm( reportForm, request);

        ParticipantBenefitBaseInformationForm theForm = (ParticipantBenefitBaseInformationForm) reportForm;
        
        ParticipantBenefitBaseDetails benefitDetails = (ParticipantBenefitBaseDetails) request.getAttribute(BDConstants.BENEFIT_DETAILS);
        if (benefitDetails != null) {
            theForm.setAsOfDate(benefitDetails.getAsOfDate());
        }
        
        Date bbBatchRundate = theForm.getAsOfDate(); // used by date range and footnote logic, default to as of date if not obtainable
        BenefitBaseBatchStatus benefitBaseBatchStatus = null;
        try {
            AccountServiceDelegateAdaptor asd = new AccountServiceDelegateAdaptor();
            benefitBaseBatchStatus = asd
                    .getBenefitBaseBatchStatusDetails(BDConstants.BD_APPLICATION_ID);
            if (benefitBaseBatchStatus != null && benefitBaseBatchStatus.getCycleDate() != null) {
                bbBatchRundate = benefitBaseBatchStatus.getCycleDate();
            }
        } catch (Exception sue) {
            logger
                    .error("ParticipantBenefitBaseInformationAction -> populateReportForm -> getBenefitBaseBatchStatusDetails");
        }
            
        // Set default FROM and TO dates
        if (theForm.getToDate() == null || theForm.getFromDate() == null) {

            try {
                theForm.setToDate(
                        DateRender.formatByPattern(
                                bbBatchRundate,
                                "",
                                RenderConstants.MEDIUM_MDY_SLASHED));

                theForm.setFromDate(
                        getDefaultFromDate(
                                bbBatchRundate));

            } catch (Exception exception) {
                exception.getCause().getMessage();
            }
        }

        ParticipantGiflData participantGiflData = (ParticipantGiflData) request
                .getAttribute(BDConstants.ACCOUNT_DETAILS);

        if (participantGiflData != null) {
            String strLastStepUpDate = DateRender.formatByPattern(
                    participantGiflData.getGiflLastStepUpDate(), "",
                    RenderConstants.MEDIUM_YMD_DASHED);

            // To set the showFootnote flag
            if (!strLastStepUpDate.equals(BDConstants.DEFAULT_DATE)
                    && participantGiflData.getGiflLastStepUpDate().compareTo(
                            bbBatchRundate) > 0) {
                theForm.setShowFootnote("Y");
            } else {
                theForm.setShowFootnote("N");
            }

                // checking for benefit base batch out of date status
                if (benefitBaseBatchStatus != null
                        && BDConstants.BB_BATCH_STATUS
                                .equals(benefitBaseBatchStatus
                                        .getBenefitBaseBatchStatus())) {
                    theForm.setBbBatchDateLessThenETL(BDConstants.YES);
                } else {
                    theForm.setBbBatchDateLessThenETL(BDConstants.NO);
                }

            // trade restriction information needs to be re-populated in form
            // here
            TradeRestriction tradingRestriction = (TradeRestriction) request
                    .getAttribute(BDConstants.TRADING_RESTRICTION);
            // Trading Expiration date will only be displayed if the this date
            // is in effect and it should be after GIFL selection activation
            // date
            if (tradingRestriction.isTradeRestrictionInEffect()
                    && tradingRestriction.getTradeRestrictionStartDate().after(
                            participantGiflData.getGiflSelectionDate())) {
                theForm.setShowTradingExpirationDate(BDConstants.YES);
                String tradingRestrictionExpDate = DateRender.formatByPattern(
                        tradingRestriction.getTradeRestrictionEndDate(), "",
                        RenderConstants.MEDIUM_YMD_DASHED);
                if (BDConstants.DEFAULT_DATE.equals(tradingRestrictionExpDate)
                        || "".equals(tradingRestrictionExpDate)) {
                    tradingRestrictionExpDate = BDConstants.NA;
                } else {
                    tradingRestrictionExpDate = DateRender.formatByPattern(
                            tradingRestriction.getTradeRestrictionEndDate(),
                            "", RenderConstants.EXTRA_LONG_MDY);
                }
                theForm
                        .setDisplayTradingExpirationDate(tradingRestrictionExpDate);
            } else {
                theForm.setShowTradingExpirationDate(BDConstants.NO);
            }
            
            // Set Gifl Selection date
            if (theForm.getGiflSelectionDate() == null ) {
                    theForm.setGiflSelectionDate(DateRender.formatByPattern(participantGiflData.getGiflSelectionDate(),
                            "", RenderConstants.MEDIUM_MDY_SLASHED));
            }
            theForm.setRateForLastIncomeEnhancement(participantGiflData
					.getDisplayRateForLastIncomeEnhancement());
        }
        
		// populating LIA details to reportFrorm
		LifeIncomeAmountDetailsVO lifeIncomeAmountDetails = (LifeIncomeAmountDetailsVO) request
				.getAttribute(BDConstants.LIA_DETAILS);
		populateParticipantLIADetailsToForm(theForm, lifeIncomeAmountDetails);
		
		 if (logger.isDebugEnabled()) {
             logger.debug("exit <- populateReportForm");
         }
    }

    /**
     * This method is used for getting ReportId
     * 
     * @return ReportId String
     */
    protected String getReportId() {
        return ParticipantBenefitBaseInformationReportData.REPORT_ID;
    }

    /**
     * This method is used for getting ReportName
     * 
     * @return ReportName String
     */
    protected String getReportName() {
        return ParticipantBenefitBaseInformationReportData.REPORT_NAME;
    }
    
    /**
	 * This method will return the File Name of the CSV file.
	 * 
	 * The CSV file will be of the Format"GuaranteedIncomeFeatureDetails- <contractnumber> - <participantlastname> 
	 * - <mmddyyyy>"
	 */
	protected String getFileName(BaseReportForm form,
			HttpServletRequest request) {
		ParticipantBenefitBaseInformationForm bbForm = (ParticipantBenefitBaseInformationForm) form;
		Date asOfDate = bbForm.getAsOfDate();
		String participantLastName = "";
		ParticipantBenefitBaseDetails benefitDetails = (ParticipantBenefitBaseDetails) request
				.getAttribute(BDConstants.BENEFIT_DETAILS);
		if (benefitDetails != null) {
			participantLastName = benefitDetails.getLastName();
		}
		StringBuilder csvFileName = new StringBuilder(CSV_PARTIAL_FILE_NAME);
		csvFileName.append(BDConstants.HYPHON_SYMBOL);
		csvFileName.append(getBobContext(request).getCurrentContract()
				.getContractNumber());
		csvFileName.append(BDConstants.HYPHON_SYMBOL);
		csvFileName.append(participantLastName);
		csvFileName.append(BDConstants.HYPHON_SYMBOL);
		csvFileName.append(DateRender.formatByPattern(asOfDate, "",
				BDConstants.DATE_FORMAT_MMDDYYYY));
		csvFileName.append(CSV_EXTENSION);
		return csvFileName.toString();
	}    

    /**
     * This method is used for specifying the ReportCriteria
     * 
     * @param criteria
     *            ReportCriteria
     * @param reportForm
     *            BaseReportForm
     * @param request
     *            HttpServletRequest
     * @throws SystemException
     */
    protected void populateReportCriteria(ReportCriteria criteria,
            BaseReportForm form, HttpServletRequest request)
            throws SystemException {

        Boolean requiredReportData;
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> populateReportCriteria");
        }

        ParticipantBenefitBaseInformationForm theForm = (ParticipantBenefitBaseInformationForm) form;

        request.setAttribute(BDConstants.PARTICIPANT_ID, theForm
                .getParticipantId());

        SimpleDateFormat format = new SimpleDateFormat(FORMAT_DATE_SHORT_MDY);

        BobContext bobContext = getBobContext(request);
        Contract currentContract = bobContext.getCurrentContract();

        if (theForm.getProfileId() == null) {
            theForm
                    .setProfileId(request
                            .getParameter(ParticipantBenefitBaseInformationForm.PARAMETER_KEY_PROFILE_ID));
        }
        // This logic is modified, since the variable ParticipantGiflData needs
        // to be removed from instance scope
        if (theForm.getParticipantId() == null
                || theForm.getParticipantId().length() == 0) {
            ParticipantGiflData participantGiflData = (ParticipantGiflData) request
                    .getAttribute(BDConstants.ACCOUNT_DETAILS);
            theForm.setParticipantId(participantGiflData.getParticipantId()
                    .toString());
        }

        if (theForm.getProposalNumber() == null
                || theForm.getProposalNumber().length() == 0) {

            String proposalNumber = currentContract.getProposalNumber();
            theForm.setProposalNumber(proposalNumber);
        }

        // Get the from Date
        if (!StringUtils.isEmpty(theForm.getFromDate())) {
            try {

                Date fromDate = format.parse(theForm.getFromDate());
                criteria
                        .addFilter(
                                ParticipantBenefitBaseInformationReportData.FILTER_FROM_DATE,
                                fromDate);
            } catch (ParseException parseException) {
                if (logger.isDebugEnabled()) {
                    logger.debug("ParseException in fromDate "
                            + "populateReportCriteria() "
                            + "ParticipantBenefitBaseInformationAction:",
                            parseException);
                }
            }
        }

        // Get the to Date
        if (!StringUtils.isEmpty(theForm.getToDate())) {
            try {

                Date toDate = format.parse(theForm.getToDate());
                criteria
                        .addFilter(
                                ParticipantBenefitBaseInformationReportData.FILTER_TO_DATE,
                                toDate);
            } catch (ParseException parseException) {
                if (logger.isDebugEnabled()) {
                    logger
                            .debug(
                                    "ParseException in toDate populateReportCriteria() "
                                            + "ParticipantBenefitBaseInformationAction:",
                                    parseException);
                }
            }
        }
        
        // Get the Gifl Selection Date
        if (!StringUtils.isEmpty(theForm.getGiflSelectionDate())) {
            try {

                Date giflSelectionDate = format.parse(theForm.getGiflSelectionDate());
                criteria
                        .addFilter(
                                ParticipantBenefitBaseInformationReportData.FILTER_GIFL_SELECTION_DATE,
                                giflSelectionDate);
            } catch (ParseException parseException) {
                if (logger.isDebugEnabled()) {
                    logger
                            .debug(
                                    "ParseException in toDate populateReportCriteria() while parsing giflSelectionDate"
                                            + "ParticipantBenefitBaseInformationAction:",
                                    parseException);
                }
            }
        }

        // Report will be shown to both internal and external user. If we want
        // to restrict that we have to set this
        // values conditionally based on user type.
        requiredReportData = new Boolean(true);

        criteria
                .addFilter(
                        ParticipantBenefitBaseInformationReportData.FILTER_TRANSACTION_DETAILS_ON_USER,
                        requiredReportData);
        criteria.addFilter(
                ParticipantBenefitBaseInformationReportData.FILTER_AS_OF_DATE,
                theForm.getAsOfDate());

        criteria.addFilter(
                ParticipantBenefitBaseInformationReportData.FILTER_PROFILE_ID,
                theForm.getProfileId());

        criteria.addFilter(
                ParticipantBenefitBaseInformationReportData.FILTER_PRTID,
                theForm.getParticipantId());

        criteria
                .addFilter(
                        ParticipantBenefitBaseInformationReportData.FILTER_CONTRACT_NUMBER,
                        String.valueOf(currentContract.getContractNumber()));

        criteria
                .addFilter(
                        ParticipantBenefitBaseInformationReportData.FILTER_PROPOSAL_NUMBER,
                        theForm.getProposalNumber());

        criteria.addFilter(
                ParticipantBenefitBaseInformationReportData.APPLICATION_ID,
                BDConstants.BD_APPLICATION_ID);

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- populateReportCriteria");
        }
    }

    /**
     * This method is used for retrieving the participant details
     * 
     * @param contractNumber
     *            int
     * @param profileId
     *            String
     * @param request
     *            HttpServletRequest
     * @throws SystemException
     */
    private void retrieveParticipantDetails(String profileId,
            HttpServletRequest request) throws SystemException {
    	
    	try {
			ParticipantBenefitBaseDetails participantBenefitBaseDetails = new ParticipantBenefitBaseDetails();

			int contractNumber = getBobContext(request).getCurrentContract()
					.getContractNumber();

			int participantId = ParticipantServiceDelegate.getInstance()
					.getParticipantIdByProfileId(Long.parseLong(profileId),
							contractNumber);

			// Getting Employee related information like firstName,lastName,
			// SSN, D.O.B
			Employee employee = employeeServiceDelegate.getEmployeeByProfileId(
					Long.parseLong(profileId), contractNumber, null);

			String contractNo = new Integer(contractNumber).toString();

			EmployeeDetailVO employeeDetailVO = employee.getEmployeeDetailVO();

			if (employeeDetailVO == null) {
				throw new SystemException(
						"Problem retrieving the participant details for profileId: "
								+ profileId + "; ContractId:" + contractNumber);
			}

			participantBenefitBaseDetails.setFirstName(employeeDetailVO
					.getFirstName());

			participantBenefitBaseDetails.setLastName(employeeDetailVO
					.getLastName());

			participantBenefitBaseDetails.setMiddleInitial(employeeDetailVO
					.getMiddleInitial());

			participantBenefitBaseDetails.setSsn(employeeDetailVO
					.getSocialSecurityNumber());

			participantBenefitBaseDetails.setDateOfBirth(employeeDetailVO
					.getBirthDate());

			// To get the market value for GO funds
			GiflMarketValue gmv = null;
			try {
				gmv = AccountServiceDelegate.getInstance()
						.getParticipantGiflMarketValue(
								String.valueOf(participantId),
								String.valueOf(contractNumber));
			} catch (Exception exception) {
				throw new SystemException(
						"Unable to retrieve market value for the profileId:"
								+ profileId + "; Contract Number:" + contractNo);
			}

			participantBenefitBaseDetails.setMarketValueGoFunds(gmv
					.getMarketValue());
			participantBenefitBaseDetails.setAsOfDate(gmv.getAsOfDate());

			request.setAttribute(BDConstants.BENEFIT_DETAILS,
					participantBenefitBaseDetails);
		} catch (Exception exception) {
			throw new SystemException(
					"Exception in retrieveParticipantDetails : "
							+ exception.getMessage());
		}    	
        
    }

    /**
     * Check if the profileId was provided and if not retrieve using the
     * participantId
     * 
     * @param participantBenefitBaseInformationForm
     *            ParticipantBenefitBaseInformationForm
     * @throws SystemException
     */
    public void populateProfileId(
            ParticipantBenefitBaseInformationForm participantBenefitBaseInformationForm)
            throws SystemException {

        if (participantBenefitBaseInformationForm.getProfileId() == null
                || participantBenefitBaseInformationForm.getProfileId()
                        .length() == 0) {

            if (participantBenefitBaseInformationForm.getParticipantId() != null
                    && participantBenefitBaseInformationForm.getParticipantId()
                            .length() > 0) {

                AccountServiceDelegateAdaptor asd = new AccountServiceDelegateAdaptor();

                participantBenefitBaseInformationForm
                        .setProfileId(asd
                                .getProfileIdByParticipantId(participantBenefitBaseInformationForm
                                        .getParticipantId()));
            }

            if (participantBenefitBaseInformationForm.getProfileId() == null
                    || participantBenefitBaseInformationForm.getProfileId()
                            .length() == 0) {
                Exception ex = new Exception("Failed to get the profileId");
                throw new SystemException(ex,
                        "Exception in populateProfileId.Failed to get profileId on form "
                                + participantBenefitBaseInformationForm
                                        .toString());
            }
        }
    }

    /**
     * To calculate the default from date
     * 
     * @param lastETLRunDate
     *            Date
     * @return String
     */
    public String getDefaultFromDate(Date lastETLRunDate) {

        // Create a Calendar object for the last ETL run date
        Calendar lastETLCalendar = Calendar.getInstance();
        lastETLCalendar.setTime(lastETLRunDate);

        // Subtracting one month from the last ETL run date
        lastETLCalendar.add(Calendar.MONTH, -1);

        // Return the calculated default from date as a String
        return DateRender.formatByPattern(lastETLCalendar.getTime(), "",
                RenderConstants.MEDIUM_MDY_SLASHED);
    }
    
     /**
     * @See BDReportAction#prepareXMLFromReport()
     */
    @SuppressWarnings("unchecked")
    @Override
    public Document prepareXMLFromReport(BaseReportForm reportForm, ReportData report,
            HttpServletRequest request) throws ParserConfigurationException {
        
        ParticipantBenefitBaseInformationReportData data = (ParticipantBenefitBaseInformationReportData) report;
        ParticipantBenefitBaseInformationForm theForm = (ParticipantBenefitBaseInformationForm) reportForm;

        PDFDocument doc = new PDFDocument();

        LayoutPage layoutPageBean = getLayoutPage(BDPdfConstants.BENEFIT_BASE_PATH, request);

        Element rootElement = doc.createRootElement(BDPdfConstants.BENEFIT_BASE);

        setIntroXMLElements(layoutPageBean, doc, rootElement, request);
        
        if (BDConstants.YES.equals(theForm.getBbBatchDateLessThenETL())) {
            String message = ContentHelper.getContentText(BDContentConstants.MISCELLANEOUS_BENEFIT_BASE_BATCH_OUT_OF_DATE, 
                    ContentTypeManager.instance().MISCELLANEOUS, null);
            PdfHelper.convertIntoDOM(BDPdfConstants.MESSAGE_TEXT, rootElement, doc, message);
        }

        String bodyHeader1 = ContentUtility.getContentAttributeText(layoutPageBean,
                BDContentConstants.BODY1_HEADER, null);
        PdfHelper.convertIntoDOM(BDPdfConstants.BODY_HEADER1, rootElement, doc, bodyHeader1);
        
        String bodyHeader2 = ContentUtility.getContentAttributeText(layoutPageBean,
                BDContentConstants.BODY2_HEADER, null);
        PdfHelper.convertIntoDOM(BDPdfConstants.BODY_HEADER2, rootElement, doc, bodyHeader2);
        
        // Lifetime Income Amount Summary Header
        String bodyHeader3 = ContentUtility.getContentAttributeText(layoutPageBean,
                BDContentConstants.BODY3_HEADER, null);
        PdfHelper.convertIntoDOM(BDPdfConstants.BODY_HEADER3, rootElement, doc, bodyHeader3);
        
        Contract currentContract = getBobContext(request).getCurrentContract();
        
        setSummaryInfoXMLElements(doc, rootElement, theForm, request, currentContract);
        
        doc.appendTextNode(rootElement, BDPdfConstants.FROM_DATE, theForm.getFromDate());
        doc.appendTextNode(rootElement, BDPdfConstants.TO_DATE, theForm.getToDate());
        doc.appendTextNode(rootElement, BDPdfConstants.ASOF_DATE, DateRender.formatByPattern(
            currentContract.getContractDates().getAsOfDate(), null, RenderConstants.MEDIUM_MDY_SLASHED));
        
        int noOfRows = getNumberOfRowsInReport(report);
        if (!theForm.isHasError()) {
            if (noOfRows > 0 && data != null) {
                // Transaction Details - start
                Element txnDetailsElement = doc.createElement(BDPdfConstants.TXN_DETAILS);
                Element txnDetailElement;
                Iterator iterator = data.getDetails().iterator();
                int maxRowsinPDF = theForm.getCappedRowsInPDF();
                int rowCount = 0;
                for (int i = 0; i < noOfRows && rowCount <= maxRowsinPDF; i++) {   
                    txnDetailElement = doc.createElement(BDPdfConstants.TXN_DETAIL);
                    ParticipantBenefitBaseInformationDataItem theItem = (ParticipantBenefitBaseInformationDataItem) iterator.next();
                    // Sets main report.
                    setReportDetailsXMLElements(doc, txnDetailElement, theItem);
                    doc.appendElement(txnDetailsElement, txnDetailElement);
                    rowCount++;
                }
                doc.appendElement(rootElement, txnDetailsElement);
                // Transaction Details - end
            } else {    
                String message = ContentHelper.getContentText(BDContentConstants.NO_TRANSACTIONS_MESSAGE_FOR_BENEFIT_BASE_PAGE, 
                            ContentTypeManager.instance().MISCELLANEOUS, null);
                PdfHelper.convertIntoDOM(BDPdfConstants.MSG_NO_TXN, rootElement, doc, message);
            }
        }
        
        if (BDConstants.YES.equals(theForm.getShowFootnote())) {
            doc.appendTextNode(rootElement, BDPdfConstants.SHOW_FOOTNOTE, null);
            String footnote = null;
            if (BDConstants.GIFL_VERSION_03.equals(currentContract.getGiflVersion())) {
                footnote = ContentHelper.getContentText(BDContentConstants.BENEFIT_BASE_PAGE_DYNAMIC_INCOME_ENHANCEMENT_FOOTNOTE, 
                        ContentTypeManager.instance().DISCLAIMER, null);
                
            } else {
                footnote = ContentHelper.getContentText(BDContentConstants.BENEFIT_BASE_PAGE_DYNAMIC_STEP_UP_FOOTNOTE, 
                        ContentTypeManager.instance().DISCLAIMER, null);
            }
            
            PdfHelper.convertIntoDOM(BDPdfConstants.FOOTNOTE_BB, rootElement, doc, footnote);
        }
        
        setFooterXMLElements(layoutPageBean, doc, rootElement, request);
 
        return doc.getDocument();
    }

    /**
     * This method sets summary information XML elements
     * 
     * @param doc
     * @param rootElement
     * @param theForm
     * @param request
     * @param currentContract
     */
    private void setSummaryInfoXMLElements(PDFDocument doc, Element rootElement, ParticipantBenefitBaseInformationForm theForm,
            HttpServletRequest request, Contract currentContract)throws ParserConfigurationException {
        
        ParticipantBenefitBaseDetails participantBenefitBaseDetails = (ParticipantBenefitBaseDetails) request.getAttribute(BDConstants.BENEFIT_DETAILS);

        WebParticipantGiflData participantGiflData = (WebParticipantGiflData) request.getAttribute(BDConstants.ACCOUNT_DETAILS);

        Element summaryInfoElement = doc.createElement(BDPdfConstants.SUMMARY_INFO);

        doc.appendTextNode(summaryInfoElement, BDPdfConstants.NAME, participantBenefitBaseDetails.getName());
        doc.appendTextNode(summaryInfoElement, BDPdfConstants.PPT_SSN, SSNRender.format(
                participantBenefitBaseDetails.getSsn(), null));
        if (participantBenefitBaseDetails.getDateOfBirth() != null) {
            doc.appendTextNode(summaryInfoElement, BDPdfConstants.DATE_OF_BIRTH, DateRender.formatByPattern(
                    participantBenefitBaseDetails.getDateOfBirth(), null, RenderConstants.MEDIUM_MDY_SLASHED));
        }
        doc.appendTextNode(summaryInfoElement, BDPdfConstants.AMT, NumberRender.formatByType(participantGiflData
                .getGiflBenefitBaseAmt(), null, RenderConstants.CURRENCY_TYPE));
        
        if (BDConstants.DEFAULT_DATE.equals(participantGiflData.getWebGiflDeselectionDate())) {
            doc.appendTextNode(summaryInfoElement, BDPdfConstants.MARKET_VALUE, NumberRender.formatByType(participantBenefitBaseDetails
                    .getMarketValueGoFunds(), null, RenderConstants.CURRENCY_TYPE));
        } else {
            doc.appendTextNode(summaryInfoElement, BDPdfConstants.MARKET_VALUE, NumberRender.formatByType(ZERO, null, RenderConstants.CURRENCY_TYPE));
            doc.appendTextNode(summaryInfoElement, BDPdfConstants.DESELECTION_DATE, participantGiflData.getWebGiflDeselectionDate());
        }
        
        doc.appendTextNode(summaryInfoElement, BDPdfConstants.SELECTION_DATE, participantGiflData.getWebGiflSelectionDate());
        
        doc.appendTextNode(summaryInfoElement, BDPdfConstants.ACTIVATION_DATE, participantGiflData.getWebGiflActivationDate());
        
        doc.appendTextNode(summaryInfoElement, BDPdfConstants.NEXT_STEP_UP_DATE, 
                        participantGiflData.getWebGiflNextStepUpDate());
        
        doc.appendTextNode(summaryInfoElement, BDPdfConstants.HOLDING_PERIOD_EXP_DATE, 
                        participantGiflData.getWebGiflHoldingPeriodExpDate());
        
        if (currentContract.getHasContractGatewayInd()) {
            if (BDConstants.YES.equals(theForm.getShowTradingExpirationDate())) {
                doc.appendTextNode(summaryInfoElement, BDPdfConstants.TRADE_RESTRICTION_DATE, theForm.getDisplayTradingExpirationDate());

            } else if (BDConstants.GIFL_VERSION_03.equals(currentContract.getGiflVersion())) {
                doc.appendTextNode(summaryInfoElement, BDPdfConstants.LAST_INCOME_ENHANCEMENT_RATE, theForm.getRateForLastIncomeEnhancement());
            }
        }
        doc.appendTextNode(summaryInfoElement, BDPdfConstants.LAST_STEP_UP_DATE, participantGiflData.getWebGiflLastStepUpDate());
        if (BDConstants.NA.equals(participantGiflData.getWebGiflLastStepUpChangeAmt())) {
            doc.appendTextNode(summaryInfoElement, BDPdfConstants.LAST_STEP_UP_AMT, participantGiflData
                    .getWebGiflLastStepUpChangeAmt());
        } else {
            doc.appendTextNode(summaryInfoElement, BDPdfConstants.LAST_STEP_UP_AMT, NumberRender.formatByType(participantGiflData
                    .getGiflLastStepUpChangeAmt(), null, RenderConstants.CURRENCY_TYPE));
        }
        
		doc.appendTextNode(summaryInfoElement,
				BDPdfConstants.SHOW_LIA_SUMMARY_INFO, String
						.valueOf(theForm.isShowLIADetailsSection()));
		if (theForm.isShowLIADetailsSection()) {
			setLIAInfoXMLElements(doc, rootElement, theForm,
					summaryInfoElement);
		}
        
        doc.appendElement(rootElement, summaryInfoElement);
       
    }
        
    /**
     * This method sets report details XML elements
     * 
     * @param doc
     * @param txnDetailElement
     * @param theItem
     */
    private void setReportDetailsXMLElements(PDFDocument doc, Element txnDetailElement,
        ParticipantBenefitBaseInformationDataItem theItem) {
        
        doc.appendTextNode(txnDetailElement, BDPdfConstants.TXN_DATE, DateRender
                .formatByStyle(theItem.getTransactionEffectiveDate(), null, RenderConstants.MEDIUM_STYLE));
        if (theItem.getTransactionNumber().doubleValue() == 0) {
            doc.appendTextNode(txnDetailElement, BDPdfConstants.TXN_NUMBER, null);
        } else {
            doc.appendTextNode(txnDetailElement, BDPdfConstants.TXN_NUMBER, theItem.getTransactionNumber().toString());
        }
        doc.appendTextNode(txnDetailElement, BDPdfConstants.TXN_TYPE, theItem.getTransactionType());
        if (BDConstants.NA.equals(theItem.getMarketValueBeforeTransaction())) {
            doc.appendTextNode(txnDetailElement, BDPdfConstants.MARKET_VALUE, BDConstants.HYPHON_SYMBOL);
        } else {
            doc.appendTextNode(txnDetailElement, BDPdfConstants.MARKET_VALUE, theItem.getMarketValueBeforeTransaction());
        }
        
        if (BDConstants.NA.equals(theItem.getTransactionAmount())) {
            doc.appendTextNode(txnDetailElement, BDPdfConstants.TXN_AMT, BDConstants.HYPHON_SYMBOL);
        } else {
            doc.appendTextNode(txnDetailElement, BDPdfConstants.TXN_AMT, theItem.getTransactionAmount());
        }
        
        if (BDConstants.NA.equals(theItem.getBenefitBaseChangeAmount())) {
            doc.appendTextNode(txnDetailElement, BDPdfConstants.BB_CHANGE_AMT, BDConstants.HYPHON_SYMBOL);
        } else {
            doc.appendTextNode(txnDetailElement, BDPdfConstants.BB_CHANGE_AMT, theItem.getBenefitBaseChangeAmount());
        }
        doc.appendTextNode(txnDetailElement, BDPdfConstants.AMT, NumberRender.formatByType(theItem
                .getBenefitBaseAmount(), null, RenderConstants.DECIMAL_TYPE));
        doc.appendTextNode(txnDetailElement, BDPdfConstants.HOLDING_PERIOD_IND, theItem.getHoldingPeriodInd());
        
    }

    /**
     * @See BaseReportAction#getXSLTFileName()
     */
    @Override
    public String getXSLTFileName() {
        return XSLT_FILE_KEY_NAME;
    }
    
    /**
     * @See BaseReportAction#doPrintPDF()
     */
    @RequestMapping(value ="/participant/participantBenefitBaseInformation/",params={"task=printPDF"}   , method =  {RequestMethod.GET}) 
    public String doPrintPDF (@Valid @ModelAttribute("participantBenefitBaseInformationForm") ParticipantBenefitBaseInformationForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	  String forwardPreExecute= preExecute(form, request, response);
          if (forwardPreExecute != null) {
       	  return StringUtils.contains(forwardPreExecute, '/') ? forwardPreExecute : forwards.get(forwardPreExecute);
          }
    	if(bindingResult.hasErrors()){
    		request.setAttribute(BDConstants.BENEFIT_DETAILS,
                    new ParticipantBenefitBaseDetails());
            request.setAttribute(BDConstants.ACCOUNT_DETAILS,
                    new WebParticipantGiflData());
            request.setAttribute(BDConstants.LIA_DETAILS,
					new LifeIncomeAmountDetailsVO());
            request.setAttribute(BDConstants.TECHNICAL_DIFFICULTIES, YES);
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
        
        Collection<GenericException> errorsValidate = doValidate(form,request);
		if (!errorsValidate.isEmpty()) {
			BDSessionHelper.setErrorsInSession(request, errorsValidate);
			 return forwards.get("input");
        
		}
        Collection errors = validateTransactionHistory((ParticipantBenefitBaseInformationForm) form, request);
        if (errors.isEmpty()) {
            super.doPrintPDF( form, request, response);
        } else {
          
            form.setHasError(true);
            ByteArrayOutputStream pdfOutStream = prepareXMLandGeneratePDF(form, null, request);
                
            response.setHeader("Cache-Control", "must-revalidate");
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
        }
        return null;
    }
    
    @RequestMapping(value ="/participant/participantBenefitBaseInformation/", params={"task=sort"}, method ={RequestMethod.GET}) 
    public String doSort (@Valid @ModelAttribute("participantBenefitBaseInformationForm") ParticipantBenefitBaseInformationForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    	    throws IOException,ServletException, SystemException {
    	  String forwardPreExecute= preExecute(form, request, response);
          if (forwardPreExecute != null) {
       	  return StringUtils.contains(forwardPreExecute, '/') ? forwardPreExecute : forwards.get(forwardPreExecute);
          }
    	if(bindingResult.hasErrors()){
    		request.setAttribute(BDConstants.BENEFIT_DETAILS,
                    new ParticipantBenefitBaseDetails());
            request.setAttribute(BDConstants.ACCOUNT_DETAILS,
                    new WebParticipantGiflData());
            request.setAttribute(BDConstants.LIA_DETAILS,
					new LifeIncomeAmountDetailsVO());
            request.setAttribute(BDConstants.TECHNICAL_DIFFICULTIES, YES);
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
          
          Collection<GenericException> errors = doValidate(form,request);
  		if (!errors.isEmpty()) {
  			BDSessionHelper.setErrorsInSession(request, errors);
  			 return forwards.get("input");
          
  		}
    	String forward = super.doSort(form, request, response);
        return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
    }
    @RequestMapping(value ="/participant/participantBenefitBaseInformation/", params={"task=page"}, method ={RequestMethod.GET}) 
    public String doPage (@Valid @ModelAttribute("participantBenefitBaseInformationForm") ParticipantBenefitBaseInformationForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    	    throws IOException,ServletException, SystemException {
    	  String forwardPreExecute= preExecute(form, request, response);
          if (forwardPreExecute != null) {
       	  return StringUtils.contains(forwardPreExecute, '/') ? forwardPreExecute : forwards.get(forwardPreExecute);
          }
    	/*if(bindingResult.hasErrors()){
    		request.setAttribute(BDConstants.BENEFIT_DETAILS,
                    new ParticipantBenefitBaseDetails());
            request.setAttribute(BDConstants.ACCOUNT_DETAILS,
                    new WebParticipantGiflData());
            request.setAttribute(BDConstants.LIA_DETAILS,
					new LifeIncomeAmountDetailsVO());
            request.setAttribute(BDConstants.TECHNICAL_DIFFICULTIES, YES);
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}*/
          
          Collection<GenericException> errors = doValidate(form,request);
  		if (!errors.isEmpty()) {
  			BDSessionHelper.setErrorsInSession(request, errors);
  			 return forwards.get("input");
          
  		}
    	String forward = super.doPage(form, request, response);
        return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
    }
    @RequestMapping(value ="/participant/participantBenefitBaseInformation/", params={"task=print"}, method ={RequestMethod.GET}) 
    public String doPrint (@Valid @ModelAttribute("participantBenefitBaseInformationForm") ParticipantBenefitBaseInformationForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    	    throws IOException,ServletException, SystemException {
    	  String forwardPreExecute= preExecute(form, request, response);
          if (forwardPreExecute != null) {
       	  return StringUtils.contains(forwardPreExecute, '/') ? forwardPreExecute : forwards.get(forwardPreExecute);
          }
    	if(bindingResult.hasErrors()){
    		request.setAttribute(BDConstants.BENEFIT_DETAILS,
                    new ParticipantBenefitBaseDetails());
            request.setAttribute(BDConstants.ACCOUNT_DETAILS,
                    new WebParticipantGiflData());
            request.setAttribute(BDConstants.LIA_DETAILS,
					new LifeIncomeAmountDetailsVO());
            request.setAttribute(BDConstants.TECHNICAL_DIFFICULTIES, YES);
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
          
          Collection<GenericException> errors = doValidate(form,request);
  		if (!errors.isEmpty()) {
  			BDSessionHelper.setErrorsInSession(request, errors);
  			 return forwards.get("input");
          
  		}
    	String forward = super.doPrint(form, request, response);
        return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
    }
    @RequestMapping(value ="/participant/participantBenefitBaseInformation/", params={"task=filter"}, method ={RequestMethod.GET}) 
    public String doFilter (@Valid @ModelAttribute("participantBenefitBaseInformationForm") ParticipantBenefitBaseInformationForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    	    throws IOException,ServletException, SystemException {
    	  String forwardPreExecute= preExecute(form, request, response);
          if (forwardPreExecute != null) {
       	  return StringUtils.contains(forwardPreExecute, '/') ? forwardPreExecute : forwards.get(forwardPreExecute);
          }
    	if(bindingResult.hasErrors()){
    		request.setAttribute(BDConstants.BENEFIT_DETAILS,
                    new ParticipantBenefitBaseDetails());
            request.setAttribute(BDConstants.ACCOUNT_DETAILS,
                    new WebParticipantGiflData());
            request.setAttribute(BDConstants.LIA_DETAILS,
					new LifeIncomeAmountDetailsVO());
            request.setAttribute(BDConstants.TECHNICAL_DIFFICULTIES, YES);
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
          
          Collection<GenericException> errors = doValidate(form,request);
  		if (!errors.isEmpty()) {
  			BDSessionHelper.setErrorsInSession(request, errors);
  			 return forwards.get("input");
          
  		}
    	String forward = super.doFilter(form, request, response);
        return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
    }
    
    /**
     * This method is used to get the Trade Restriction message if any for the
     * given participant
     * 
     * @param request
     * @return TradeRestriction a tradeRestriction object with effective trade
     *         restriction details
     * @throws Exception
     */
    private TradeRestriction getTradingRestriction(Long participantId,
            Integer proposalNumber) throws Exception {
        TradeRestriction tradeRestriction = employeeServiceDelegate
                .getGatewayTradeRestriction(participantId, proposalNumber,
                        AccountServiceDelegate.getInstance()
                                .getNextNYSEClosureDate(null));
        return tradeRestriction;
    }
    
    /**
	 * to retrieve the Life Income Amount Details for the Participant
	 * 
	 * @param profileId
	 * @param request
	 * @return 
	 */
	private void getParticipantLIADetails(String profileId,
			HttpServletRequest request) throws SystemException {
		LifeIncomeAmountDetailsVO liaDetails = ContractServiceDelegate
				.getInstance().getLIADetailsByProfileId(profileId);
		request.setAttribute(BDConstants.LIA_DETAILS, liaDetails);

	}	

    /**
	 * to populate the Life Income Amount Details for the Participant
	 * 
	 * @param profileId
	 * @param request
	 * @return 
	 */
	private void populateParticipantLIADetailsToForm(
			ParticipantBenefitBaseInformationForm participantBenefitBaseInformationForm,
			LifeIncomeAmountDetailsVO liaDetails) {
		participantBenefitBaseInformationForm
				.setShowLIADetailsSection(LIADisplayHelper
						.isShowLIADetailsSection(liaDetails
								.getAnniversaryDate()));
		if (participantBenefitBaseInformationForm.isShowLIADetailsSection()) {
			participantBenefitBaseInformationForm
					.setLiaSelectionDate(liaDetails.getEffectiveDate());
			participantBenefitBaseInformationForm.setLiaAnnualAmount(liaDetails
					.getAnnualAmount());
			participantBenefitBaseInformationForm
					.setLiaFrequencyCode(LIADisplayHelper
							.getDisplayFrequencyCode(liaDetails
									.getFrequencyCode()));
			participantBenefitBaseInformationForm
					.setLiaIndividualOrSpousalOption(LIADisplayHelper
							.getDisplayIndividualOrSpousalOption(liaDetails
									.getSpousalOptionInd()));
			participantBenefitBaseInformationForm
					.setLiaPercentage(LIADisplayHelper
							.getFormatedPercentage(liaDetails.getShare()));
			participantBenefitBaseInformationForm.setLiaPeriodicAmt(liaDetails
					.getPeriodicAmt());
			participantBenefitBaseInformationForm
					.setLiaAnniversaryDate(liaDetails.getAnniversaryDate());
		}

	}
    
	/**
	 * This method sets LIA information XML elements
	 * 
	 * @param doc
	 * @param rootElement
	 * @param lifeIncomeAmountDetails
	 * @param summaryInfoElement
	 */
	private void setLIAInfoXMLElements(PDFDocument doc, Element rootElement,
			ParticipantBenefitBaseInformationForm theForm, Element summaryInfoElement) throws ParserConfigurationException{
		
		// appending lia labels and messages
		try{
			
			String message = getContentMessageByCmaKey(BDContentConstants.MISCELLANEOUS_BENEFIT_BASE_LIA_MESSAGE);
            PdfHelper.convertIntoDOM(BDPdfConstants.MESSAGE_TEXT, rootElement, doc, message);
			
			doc.appendTextNode(summaryInfoElement,
					BDPdfConstants.LIA_SELECTION_DATE_LABEL,
					getContentMessageByCmaKey(BDContentConstants.MISCELLANEOUS_LIA_SELECTION_DATE_FIELD_LABEL));
			doc.appendTextNode(summaryInfoElement,
					BDPdfConstants.SPOUSAL_OPTION_LABEL,
					getContentMessageByCmaKey(BDContentConstants.MISCELLANEOUS_LIA_SPOUSAL_OPTION_FIELD_LABEL));
			doc.appendTextNode(summaryInfoElement,
					BDPdfConstants.LIA_PERCENTAGE_LABEL,
					getContentMessageByCmaKey(BDContentConstants.MISCELLANEOUS_LIA_PERCENTAGE_FIELD_LABEL));
			doc.appendTextNode(summaryInfoElement,
					BDPdfConstants.ANNUAL_LIA_AMOUNT_LABEL,
					getContentMessageByCmaKey(BDContentConstants.MISCELLANEOUS_ANNUAL_LIA_AMOUNT_FIELD_LABEL));
			doc.appendTextNode(summaryInfoElement,
					BDPdfConstants.PAYMENT_FREQUENY_LABEL,
					getContentMessageByCmaKey(BDContentConstants.MISCELLANEOUS_LIA_PAYMENT_FREQUENCY_FIELD_LABEL));
			doc.appendTextNode(summaryInfoElement,
					BDPdfConstants.LIA_ANNIVERSARY_DATE_LABEL,
					getContentMessageByCmaKey(BDContentConstants.MISCELLANEOUS_LIA_ANNIVERSARY_DATE_FIELD_LABEL));
		} catch (ContentException exception) {
			throw new ParserConfigurationException(exception.getMessage());
		}
		
		// appending lia values
		doc.appendTextNode(summaryInfoElement,
				BDPdfConstants.LIA_SELECTION_DATE_VALUE,
				DateRender
                .formatByPattern(theForm.getLiaSelectionDate(), "", RenderConstants.MEDIUM_MDY_SLASHED));
		doc.appendTextNode(summaryInfoElement, BDPdfConstants.SPOUSAL_OPTION_VALUE,
				theForm.getLiaIndividualOrSpousalOption());
		doc.appendTextNode(summaryInfoElement, BDPdfConstants.LIA_PERCENTAGE_VALUE,
				theForm.getLiaPercentage());
		if (theForm.getLiaAnnualAmount() != null) {
			doc
					.appendTextNode(summaryInfoElement,
							BDPdfConstants.ANNUAL_LIA_AMOUNT_VALUE, 
							NumberRender.formatByType(
									theForm.getLiaAnnualAmount(),
									ZERO_DOLLAR, RenderConstants.CURRENCY_TYPE));
		}
		String paymentFrequencyValue = theForm.getLiaFrequencyCode();
		if (theForm.getLiaPeriodicAmt() != null) {
			paymentFrequencyValue = paymentFrequencyValue + " - "
					+ NumberRender.formatByType(
							theForm.getLiaPeriodicAmt(),
							ZERO_DOLLAR, RenderConstants.CURRENCY_TYPE);
		}
		doc.appendTextNode(summaryInfoElement,
				BDPdfConstants.PAYMENT_FREQUENY_VALUE, paymentFrequencyValue);
		doc.appendTextNode(summaryInfoElement,
				BDPdfConstants.LIA_ANNIVERSARY_DATE_VALUE,
				DateRender
                .formatByPattern(theForm.getLiaAnniversaryDate(), "", RenderConstants.MEDIUM_MDY_SLASHED));
	}
	
	/**
	 * to get the CMA text by cma key
	 * 
	 * @param cmaKey
	 * @return String
	 */
	private String getContentMessageByCmaKey( int cmaKey)
			throws ContentException {

		Content message = ContentCacheManager.getInstance().getContentById(
				cmaKey, ContentTypeManager.instance().MISCELLANEOUS);

		return ContentUtility.getContentAttribute(message, "text");
	}
		
}