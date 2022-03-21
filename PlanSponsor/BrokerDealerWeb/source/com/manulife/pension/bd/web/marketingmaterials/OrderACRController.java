package com.manulife.pension.bd.web.marketingmaterials;

import static com.manulife.pension.bd.web.BDConstants.BD_APPLICATION_ID;
import static com.manulife.pension.bd.web.BDConstants.MAIL_PROTOCOL;
import static com.manulife.pension.bd.web.BDConstants.ZERO_STRING;
import static com.manulife.pension.bd.web.BDErrorCodes.ADDRESS_LINE1_NOT_ENTERED;
import static com.manulife.pension.bd.web.BDErrorCodes.CITY_NOT_ENTERED;
import static com.manulife.pension.bd.web.BDErrorCodes.CONTRACT_NAME_REQUIRED;
import static com.manulife.pension.bd.web.BDErrorCodes.INVALID_CONTRACT_NUMBER;
import static com.manulife.pension.bd.web.BDErrorCodes.INVALID_MONTH_AND_YEAR;
import static com.manulife.pension.bd.web.BDErrorCodes.MISSING_CONTRACT_NUMBER;
import static com.manulife.pension.bd.web.BDErrorCodes.MISSING_PHONE_NUMBER;
import static com.manulife.pension.bd.web.BDErrorCodes.NUMBER_OF_COPIES_INVALID;
import static com.manulife.pension.bd.web.BDErrorCodes.NUMBER_OF_COPIES_NOT_GREATER_THAN_ZERO;
import static com.manulife.pension.bd.web.BDErrorCodes.NUMBER_OF_COPIES_REQUIRED;
import static com.manulife.pension.bd.web.BDErrorCodes.PERSONAL_INFO_COMPANY_NAME_NOT_ENTERED;
import static com.manulife.pension.bd.web.BDErrorCodes.PHONE_EXTENSION_INVALID;
import static com.manulife.pension.bd.web.BDErrorCodes.PRESENTER_NAME_REQUIRED;
import static com.manulife.pension.bd.web.BDErrorCodes.RECIPIENT_NAME_REQUIRED;
import static com.manulife.pension.bd.web.BDErrorCodes.STATE_NOT_SELECTED_FOR_USA;
import static com.manulife.pension.bd.web.BDErrorCodes.YOUR_NAME_REQUIRED;
import static com.manulife.pension.bd.web.BDErrorCodes.ZIP_CODE_REQUIRED;
import static com.manulife.pension.bd.web.marketingmaterials.OrderACRForm.FIELD_CITY_NAME;
import static com.manulife.pension.bd.web.marketingmaterials.OrderACRForm.FIELD_CONTRACT_NAME;
import static com.manulife.pension.bd.web.marketingmaterials.OrderACRForm.FIELD_CONTRACT_NUMBER;
import static com.manulife.pension.bd.web.marketingmaterials.OrderACRForm.FIELD_EMAIL;
import static com.manulife.pension.bd.web.marketingmaterials.OrderACRForm.FIELD_NUMBER__OF_BLACK_AND_WHITE_COPIES;
import static com.manulife.pension.bd.web.marketingmaterials.OrderACRForm.FIELD_NUMBER__OF_COLOR_COPIES;
import static com.manulife.pension.bd.web.marketingmaterials.OrderACRForm.FIELD_PHONE_AREA_CODE;
import static com.manulife.pension.bd.web.marketingmaterials.OrderACRForm.FIELD_PHONE_EXTENSION;
import static com.manulife.pension.bd.web.marketingmaterials.OrderACRForm.FIELD_PRESENTER_FIRST_NAME;
import static com.manulife.pension.bd.web.marketingmaterials.OrderACRForm.FIELD_PRESENTER_LAST_NAME;
import static com.manulife.pension.bd.web.marketingmaterials.OrderACRForm.FIELD_RECIPIENT_FIRST_NAME;
import static com.manulife.pension.bd.web.marketingmaterials.OrderACRForm.FIELD_RECIPIENT_LAST_NAME;
import static com.manulife.pension.bd.web.marketingmaterials.OrderACRForm.FIELD_STATE_NAME;
import static com.manulife.pension.bd.web.marketingmaterials.OrderACRForm.FIELD_STREET_NAME;
import static com.manulife.pension.bd.web.marketingmaterials.OrderACRForm.FIELD_SUBSCRIBER_NUMBER_1;
import static com.manulife.pension.bd.web.marketingmaterials.OrderACRForm.FIELD_SUBSCRIBER_NUMBER_2;
import static com.manulife.pension.bd.web.marketingmaterials.OrderACRForm.FIELD_YOUR_FIRST_NAME;
import static com.manulife.pension.bd.web.marketingmaterials.OrderACRForm.FIELD_YOUR_LAST_NAME;
import static com.manulife.pension.bd.web.marketingmaterials.OrderACRForm.FIELD_ZIPCODE;
import static com.manulife.pension.platform.web.CommonConstants.COLON_SYMBOL;
import static com.manulife.pension.platform.web.CommonConstants.HYPHON_SYMBOL;
import static com.manulife.pension.platform.web.CommonConstants.SLASH_SYMBOL;
import static com.manulife.pension.platform.web.CommonConstants.SPACE_SYMBOL;
import static com.manulife.pension.platform.web.CommonErrorCodes.EMAIL_ADDRESS_MANDATORY;
import static com.manulife.pension.platform.web.CommonErrorCodes.EMAIL_INVALID;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import javax.mail.internet.InternetAddress;
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

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.bob.planReview.util.PlanReviewReportUtils;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWOrderAcr;
import com.manulife.pension.bd.web.validation.rules.EmailAddressRule;
import com.manulife.pension.bd.web.validation.rules.MandatoryFieldRule;
import com.manulife.pension.exception.ExceptionHandlerUtility;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.platform.web.controller.BaseAutoController;
import com.manulife.pension.platform.web.validation.rules.generic.NumericRule;
import com.manulife.pension.util.BaseEnvironment;
import com.manulife.pension.util.email.EmailMessage;
import com.manulife.pension.util.email.EmailMessageException;
import com.manulife.pension.util.email.EmailSender;
import com.manulife.pension.util.email.EmailSenderException;
import com.manulife.pension.util.log.ServiceLogRecord;
import com.manulife.pension.validator.ValidationError;
import com.manulife.pension.validator.ValidationRule;

/**
 * This is the action class for Order ACR page
 * 
 * @author Siby Thomas
 * 
 */
@Controller
@RequestMapping( value ="/orderACR")

public class OrderACRController extends BaseAutoController {
	@ModelAttribute("orderACRForm") 
	public OrderACRForm populateForm() 
	{
		return new OrderACRForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/marketingmaterial/orderACR.jsp");
		forwards.put("orderACR","/marketingmaterial/orderACR.jsp ");
		forwards.put("orderACRSubmit","redirect:/do/orderACR/?action=default "); 
		forwards.put("homePage","redirect:/do/home/" );
		}
    
    private static final String LINE_BREAK = System.getProperty("line.separator");
    
    private static final String PROPERTY_SMTP_SERVER = "mail.smtp.host";
    private static final String ACR_MAIL_TO_ADDRESS = "orderACR.toAddress";
    private static final String ACR_MAIL_SUBJECT = "ACR Request from Broker Website ";
    private static final String ACR_MAIL_FROM_ADDRESS = "orderACR.fromAddress"; 

    /**
     * Constants for the Log record
     */
    private static final String LOG_USER_PROFILE_ID = "User Profile Id=";
    private static final String LOG_DATE_OF_ACTION = "Date of Action=";
    private static final String LOG_ACTION = "Action=";
    private static final String LOG_CONTRACT_NUMBER = "Contract Number=";
    private static final String LOG_CONTRACT_NAME = "Contract Name=";
    private static final String LOG_REQUEST_MONTH_AND_YEAR = "Request month and year=";
    private static final String LOG_PRESENTER_NAME = "Presenter Name=";
    private static final String LOG_NUMBER_OF_COPIES_COLOR = "Number of Copies-colour=";
    private static final String LOG_NUMBER_OF_COPIES_BLACK_AND_WHITE = "Number of Copies-Black & White=";
    private static final String LOG_OUTPUT_TYPE = "Output=";
    
    private static final String ORDER_ACR_FORWARD = "orderACR";
    private static final String ORDER_ACR_FORWARD_SUBMIT = "orderACRSubmit";
    private static String MAIL_SERVER;
    private static final String ORDER_SUBMIT = "orderSubmit";
    
    static private InternetAddress fromAddress;
    static private InternetAddress[] toAddress = new InternetAddress[1];
    
    static {
        try {
            // get SMTP server property
            BaseEnvironment environment = new BaseEnvironment();
            fromAddress = new InternetAddress(environment.getNamingVariable(ACR_MAIL_FROM_ADDRESS,
                    null));
            toAddress[0] = new InternetAddress(environment.getNamingVariable(ACR_MAIL_TO_ADDRESS,
                    null));
            MAIL_SERVER = environment.getNamingVariable(PROPERTY_SMTP_SERVER, null);
        } catch (Exception e) {
            SystemException se = new SystemException(e, "Static block failed for ");
            throw ExceptionHandlerUtility.wrap(se);
        }
    }
    
    /**
     * Constructor.
     */
    public OrderACRController() {
        super(OrderACRController.class);
    }
    
	
	protected String preExecute( ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException, SystemException {

		String forward = super.preExecute( form, request, response);

		if (forward == null) {
			if (PlanReviewReportUtils.isPlanReviewLaunched()) {
				// if the plan review launched is 'true'
				// -- > orderACR Link should be suppressed
				// in case user has boomarked this page to navigate then
				// -- > redirects to FRW home page.
				return forwards.get("homePage");
			}
		}
		return forward;
	}

    /**
     * 
     * The method is invoked when submit button is clicked
     * 
     * @param mapping The action mapping.
     * @param actionForm The action form.
     * @param request The HTTP request.
     * @param response The HTTP response.
     * 
     * @return ActionForward The forward to process.
     * 
     * @throws IOException When an IO problem occurs.
     * @throws ServletException When an Servlet problem occurs.
     * @throws SystemException When an generic application problem occurs.
     */
	
	@RequestMapping(value ="/", params={"action=sendMail","task=sendMail"} , method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doSendMail(@Valid @ModelAttribute("orderACRForm") OrderACRForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward=preExecute(actionForm, request, response);
        if ( StringUtils.isNotBlank(forward)) {
     	    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              forwards.get("orderACR");//if input forward not //available, provided default
	       }
		}
		
        /*if (!isTokenValid(request)) {
            
             * The user clicked the refresh button so redirect it back . No need to re send mail.
             
            actionForm.reset( request);
            return forwards.get(ORDER_ACR_FORWARD);
        }*/
        
        Collection<ValidationError> errors = validate(actionForm);
        if (!errors.isEmpty()) {
            /*
             * Error are there so set them in request
             */
            setErrorsInRequest(request, errors);
            return forwards.get(ORDER_ACR_FORWARD);
        } else {
            /*
             * All validations passed . So send mail and reset token.
             */
            sendEmail(actionForm, request);
        	request.setAttribute(ORDER_SUBMIT,"submitted");
          //  resetToken(request);
            return forwards.get(ORDER_ACR_FORWARD_SUBMIT);
        }
    }

    /**
     * The method is for default action. Just forward it to the Order ACR page
     * 
     * @see BaseAutoController#doDefault(ActionMapping, AutoForm, HttpServletRequest,
     *      HttpServletResponse)
     */
	@RequestMapping(value ="/" ,method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doDefault (@Valid @ModelAttribute("orderACRForm") OrderACRForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
     	    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              forwards.get("orderACR");//if input forward not //available, provided default
	       }
		}
       // saveToken(request);
		form.reset( request);
        if(request.getAttribute(ORDER_SUBMIT)== null)
        	request.setAttribute(ORDER_SUBMIT,"Notsubmitted");
        return forwards.get(ORDER_ACR_FORWARD);
    }
	
	@Autowired
	   private BDValidatorFWOrderAcr  bdValidatorFWOrderAcr;
@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(bdValidatorFWOrderAcr);
	}
	
    /**
     * 
     * The method returns back a collection of errors after validating the form bean
     * 
     * @param form OrderACRForm
     * @return Collection<ValidationError>
     */
    private Collection<ValidationError> validate(OrderACRForm form) {
        Collection<ValidationError> errors = new ArrayList<ValidationError>();

        MandatoryFieldRule mandatoryRule = null;
        NumericRule numericRule = null;
        boolean isValid;

        /*
         * validating if contract name is empty or not
         */
        mandatoryRule = new MandatoryFieldRule(CONTRACT_NAME_REQUIRED);
        mandatoryRule.validate(FIELD_CONTRACT_NAME, errors, form.getContractName());

        /*
         * validating if contract number is empty or not
         */
        mandatoryRule = new MandatoryFieldRule(MISSING_CONTRACT_NUMBER);
        isValid = mandatoryRule.validate(FIELD_CONTRACT_NUMBER, errors, form.getContractNumber());

        /*
         * validating if contract number is numeric or not
         */
        if (isValid) {
            numericRule = new NumericRule(INVALID_CONTRACT_NUMBER);
            numericRule.validate(FIELD_CONTRACT_NUMBER, errors, form.getContractNumber().trim());
        }

        /*
         * validating if report month and year has been changed from default or not
         */
        if (ZERO_STRING.equals(form.getReportMonth())
                || OrderACRForm.YEAR_DEFAULT_VALUE.equals(form.getReportYear())) {
            errors.add(new ValidationError("reportMonthAndreportYear",
                    INVALID_MONTH_AND_YEAR));
        }

        /*
         * validating if presenter number is empty or not
         */
        mandatoryRule = new MandatoryFieldRule(PRESENTER_NAME_REQUIRED);
        validateFields(mandatoryRule, new String[] { FIELD_PRESENTER_FIRST_NAME,
                FIELD_PRESENTER_LAST_NAME }, errors, new String[] { form.getPresenterFirstName(),
                form.getPresenterLastName() });

        /*
         * validating if number of copies is empty or not
         */
        mandatoryRule = new MandatoryFieldRule(NUMBER_OF_COPIES_REQUIRED);
        isValid = validateFields(mandatoryRule, new String[] { FIELD_NUMBER__OF_COLOR_COPIES,
                FIELD_NUMBER__OF_BLACK_AND_WHITE_COPIES }, errors, new String[] {
                form.getNumberOfColorCopies(), form.getNumberOfBlackAndWhiteCopies() });

        /*
         * validating if number of copies is numeric or not
         */
        if (isValid) {
            numericRule = new NumericRule(NUMBER_OF_COPIES_INVALID);
            validateFields(numericRule, new String[] { FIELD_NUMBER__OF_COLOR_COPIES,
                    FIELD_NUMBER__OF_BLACK_AND_WHITE_COPIES }, errors, new String[] {
                    form.getNumberOfColorCopies().trim(),
                    form.getNumberOfBlackAndWhiteCopies().trim() });

        } else if (form.getNumberOfColorCopies() != null
                && !SPACE_SYMBOL.equals(form.getNumberOfColorCopies().trim())) {
            numericRule = new NumericRule(NUMBER_OF_COPIES_INVALID);
            numericRule.validate(FIELD_NUMBER__OF_COLOR_COPIES, errors, form
                    .getNumberOfColorCopies().trim());
        } else if (form.getNumberOfBlackAndWhiteCopies() != null
                && !SPACE_SYMBOL.equals(form.getNumberOfBlackAndWhiteCopies().trim())) {
            numericRule = new NumericRule(NUMBER_OF_COPIES_INVALID);
            numericRule.validate(FIELD_NUMBER__OF_BLACK_AND_WHITE_COPIES, errors, form
                    .getNumberOfBlackAndWhiteCopies().trim());
        }

        /*
         * validating if number of copies is ZERO or not
         */
        if (isValid) {
            if (ZERO_STRING.equals(form.getNumberOfColorCopies().trim())
                    && ZERO_STRING.equals(form.getNumberOfBlackAndWhiteCopies().trim())) {
                errors.add(new ValidationError("numberOfCopies",
                        NUMBER_OF_COPIES_NOT_GREATER_THAN_ZERO));
            }
        }

        /*
         * validating if presenter name is empty or not
         */
        mandatoryRule = new MandatoryFieldRule(RECIPIENT_NAME_REQUIRED);
        validateFields(mandatoryRule, new String[] { FIELD_RECIPIENT_FIRST_NAME,
                FIELD_RECIPIENT_LAST_NAME }, errors, new String[] { form.getRecipientFirstName(),
                form.getRecipientLastName() });

        /*
         * validating if company name is empty or not
         */
        mandatoryRule = new MandatoryFieldRule(PERSONAL_INFO_COMPANY_NAME_NOT_ENTERED);
        mandatoryRule
        .validate(OrderACRForm.FIELD_COMPANY_NAME, errors, form.getCompanyName());

        /*
         * validating if street address is empty or not
         */
        mandatoryRule = new MandatoryFieldRule(ADDRESS_LINE1_NOT_ENTERED);
        mandatoryRule.validate(FIELD_STREET_NAME, errors, form.getStreetAddressName());

        /*
         * validating if city name is empty or not
         */
        mandatoryRule = new MandatoryFieldRule(CITY_NOT_ENTERED);
        mandatoryRule.validate(FIELD_CITY_NAME, errors, form.getCityAddressName());

        /*
         * validating if state name is empty or not
         */
        mandatoryRule = new MandatoryFieldRule(STATE_NOT_SELECTED_FOR_USA);
        mandatoryRule.validate(FIELD_STATE_NAME, errors, form.getStateAddressName());

        /*
         * validating if zip code is empty or not
         */
        mandatoryRule = new MandatoryFieldRule(ZIP_CODE_REQUIRED);
        mandatoryRule.validate(FIELD_ZIPCODE, errors, form.getZipCodeAddress());

        /*
         * validating if your name is empty or not
         */
        mandatoryRule = new MandatoryFieldRule(YOUR_NAME_REQUIRED);
        validateFields(mandatoryRule, new String[] { FIELD_YOUR_FIRST_NAME, FIELD_YOUR_LAST_NAME },
                errors, new String[] { form.getYourFirstName(), form.getYourLastName() });

        /*
         * validating if telephone number is empty or not
         */
        mandatoryRule = new MandatoryFieldRule(MISSING_PHONE_NUMBER);
        isValid = validateFields(mandatoryRule, new String[] { FIELD_PHONE_AREA_CODE,
                FIELD_SUBSCRIBER_NUMBER_1, FIELD_SUBSCRIBER_NUMBER_2 }, errors, new String[] {
                form.getAreaCode(), form.getSubscriberNumber1(), form.getSubscriberNumber2() });

        /*
         * validating if telephone number is numeric or not
         */
        if (isValid) {
            numericRule = new NumericRule(MISSING_PHONE_NUMBER);
            validateFields(numericRule, new String[] { FIELD_PHONE_AREA_CODE,
                    FIELD_SUBSCRIBER_NUMBER_1, FIELD_SUBSCRIBER_NUMBER_2 }, errors, new String[] {
                    form.getAreaCode().trim(), form.getSubscriberNumber1().trim(),
                    form.getSubscriberNumber2().trim() });
        }

        /*
         * validating if extension number is numeric or not
         */
        if (form.getPhoneNumberExtension() != null
                && !SPACE_SYMBOL.equals(form.getPhoneNumberExtension().trim())) {
            numericRule = new NumericRule(PHONE_EXTENSION_INVALID);
            numericRule.validate(FIELD_PHONE_EXTENSION, errors, form.getPhoneNumberExtension()
                    .trim());
        }

        /*
         * validating email field
         */
        EmailAddressRule.getInstance(EMAIL_ADDRESS_MANDATORY, EMAIL_INVALID).validate(FIELD_EMAIL,
                errors, form.getContactEmailAddress());
        
        return errors;
    }

    /**
     * The method will generate an e-mail from the data that the user has submitted
     * 
     * @param form
     * @throws SystemException
     */
    private void sendEmail(OrderACRForm form, HttpServletRequest request)
            throws SystemException {

        EmailSender sender = new EmailSender(MAIL_PROTOCOL, MAIL_SERVER, null, null);
        EmailMessage message = null;

        try {
            message = new EmailMessage(fromAddress, toAddress, null, 
            		getSubject(form), getMessageBody(form));
            sender.send(message);
            logInteraction(form, request);
        } catch (EmailMessageException e) {
            logger.error("Error forming the message :" + e);
        }
        catch (EmailSenderException e) {
            logger.error("Error sending the mail :" + e);
        }
    }

    /**
     * The method returns back the message body for the mail
     * 
     * @param form
     * @return String
     */
    private String getMessageBody(OrderACRForm form) {

        StringBuffer body = new StringBuffer();
        
        body.append("Contract Name: ").append(form.getContractName()).append(LINE_BREAK);
        body.append("Contract Number: ").append(form.getContractNumber()).append(LINE_BREAK);
        body.append("Report Month-end date (mm/yyyy): ").append(form.getReportMonth()).append(
                SLASH_SYMBOL).append(form.getReportYear()).append(LINE_BREAK);
        body.append("Presenter's Name: ").append(form.getPresenterFirstName()).append(" ").append(
                form.getPresenterLastName()).append(LINE_BREAK);
        body.append("Number of copies: Color ").append(form.getNumberOfColorCopies()).append(
                " Black and White ").append(form.getNumberOfBlackAndWhiteCopies()).append(
                LINE_BREAK);
        body.append("Output: ").append(form.getOutputType()).append(LINE_BREAK);
        
        body.append(LINE_BREAK);

        body.append("Shipping Address ").append(LINE_BREAK);
        body.append("Recipient's Name: ").append(form.getRecipientFirstName()).append(" ").append(
                form.getRecipientLastName()).append(LINE_BREAK);
        body.append("Company Name: ").append(form.getCompanyName()).append(LINE_BREAK);
        body.append("Street Address: ").append(form.getStreetAddressName()).append(LINE_BREAK);
        body.append("City: ").append(form.getCityAddressName()).append(LINE_BREAK);
        body.append("State: ").append(form.getStateAddressName()).append(LINE_BREAK);
        body.append("Zip Code: ").append(form.getZipCodeAddress()).append(LINE_BREAK);

        body.append(LINE_BREAK);

        body.append("Contact information ").append(LINE_BREAK);
        body.append("Name: ").append(form.getYourFirstName()).append(" ").append(
                form.getYourLastName()).append(LINE_BREAK);
        body.append("Telephone number: ").append(form.getAreaCode()).append(HYPHON_SYMBOL).append(
                form.getSubscriberNumber1()).append(HYPHON_SYMBOL).append(
                form.getSubscriberNumber2()).append(" ext : ").append(
                form.getPhoneNumberExtension()).append(LINE_BREAK);
        body.append("E-mail address: ").append(form.getContactEmailAddress()).append(LINE_BREAK);

        body.append(LINE_BREAK).append(LINE_BREAK);
        
        body.append("Comments: ").append(form.getComments()).append(LINE_BREAK);

        return body.toString();
    }

    /**
     * The method returns back the Mail Subject
     * 
     * @return String
     * @throws SystemException
     */
    private String getSubject(OrderACRForm form) throws SystemException {
        String subject = ACR_MAIL_SUBJECT + 
        		form.getYourFirstName() + " " + form.getYourLastName();
        return subject;
    }

    /**
     * The method returns the log data
     * 
     * @param form
     * @param request
     * @return log data
     */
    private String getLogData(OrderACRForm form, HttpServletRequest request) {
        StringBuffer data = new StringBuffer();
        long profileId = 
        	BDSessionHelper.getUserProfile(request).getBDPrincipal().getProfileId();
        
        data.append(LOG_USER_PROFILE_ID).append(profileId);
        data.append(BDConstants.COMMA_SYMBOL).append(
        		LOG_DATE_OF_ACTION).append(new Date());
        data.append(BDConstants.COMMA_SYMBOL).append(
        		LOG_ACTION).append("Order ACR Request");
        data.append(BDConstants.COMMA_SYMBOL).append(
        		LOG_CONTRACT_NUMBER).append(form.getContractNumber());
        data.append(BDConstants.COMMA_SYMBOL).append(
        		LOG_CONTRACT_NAME).append(form.getContractName());
        data.append(BDConstants.COMMA_SYMBOL).append(
        		LOG_REQUEST_MONTH_AND_YEAR).append(form.getReportMonth()).append(
        				SLASH_SYMBOL).append(form.getReportYear());
        data.append(BDConstants.COMMA_SYMBOL).append(
        		LOG_PRESENTER_NAME).append(form.getPresenterFirstName()).append(" ")
                .append(form.getPresenterLastName());
        data.append(BDConstants.COMMA_SYMBOL).append(
        		LOG_NUMBER_OF_COPIES_COLOR).append(form.getNumberOfColorCopies());
        data.append(BDConstants.COMMA_SYMBOL).append(
        		LOG_NUMBER_OF_COPIES_BLACK_AND_WHITE).append(form.getNumberOfBlackAndWhiteCopies());
        data.append(BDConstants.COMMA_SYMBOL).append(
        		LOG_OUTPUT_TYPE).append(form.getOutputType());
        return data.toString();
    }

    /**
     * This method is used to log the interaction
     * 
     * @param OrderACRForm
     * 
     */
    private void logInteraction(OrderACRForm form, HttpServletRequest request) {
        ServiceLogRecord record = new ServiceLogRecord();
        record.setApplicationId(BD_APPLICATION_ID);
        record.setData(getLogData(form, request));
        record.setMethodName(this.getClass().getName() + COLON_SYMBOL + "sendMail");
        record.setUserIdentity(getUserIdentity(request));
        record.setMilliSeconds(new Date().getTime());
        record.setServiceName("Order ACR Request");
        
        /*
         * Log the record to MRL
         */
        logger.error(record);
    }

    /**
     * Validates a set of fields and adds an single error if any of them is invalid
     * 
     * @param fieldIds
     * @param validationErrors
     * @param objectsToValidate
     * @return boolean
     */
    @SuppressWarnings("unchecked")
    private boolean validateFields(ValidationRule rule, String[] fieldIds,
            Collection validationErrors,
            Object[] objectsToValidate) {
        boolean isValid = true;
        int count = 0;
        for (Object object : objectsToValidate) {
            isValid = rule.validate(fieldIds[count++], validationErrors, object);
            /*
             * return false if an error is added
             */
            if (!isValid) {
                return false;
            }
        }
        return isValid;
    }

    /**
     * The method return the user profile identity
     * 
     * @param HttpServletRequest request
     * 
     * @return BDUserProfile
     */
    private String getUserIdentity(HttpServletRequest request) {
          String userIdentity = "";
          BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);
          if(userProfile != null){
              userIdentity = userProfile.getBDPrincipal().getProfileId() + COLON_SYMBOL
                    + userProfile.getBDPrincipal().getUserName();
          }
          return userIdentity;
    }
}
