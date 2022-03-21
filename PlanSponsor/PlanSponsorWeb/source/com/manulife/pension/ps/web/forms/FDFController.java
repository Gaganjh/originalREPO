package com.manulife.pension.ps.web.forms;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.util.DataUtility;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.DynaForm;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.tools.EmployeeLetterHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.contract.valueobject.DayOfYear;
import com.manulife.pension.service.plan.valueobject.PlanDataLite;
import com.manulife.util.pdf.FDFDocument;
import com.manulife.util.pdf.FDFField;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.RenderConstants;


@Controller
@RequestMapping( value = "/resources/FDFAction.fdf")
public class FDFController extends PsController {
	@ModelAttribute("dynaForm") 
	public DynaForm populateForm() 
	{
		return new DynaForm();
		}
	
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/resources/forms.jsp");
		forwards.put("forms","/resources/forms.jsp");
	}
	
	
	
	
	private static final FastDateFormat DATE_FORMATTER = FastDateFormat.getInstance("yyyy/MM/dd");
	private static final DateFormat CSF_ANNIVERSARY_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	private static final FastDateFormat SHORT_DATE_FORMAT = FastDateFormat.getInstance("MMMM dd");
	
	private static final String OR_STRING = " or ";
	private static final String NEXT_PLAN_ENTRY_DATE_PARAM = "nped"; 
    public static final String BGA_CONTRACT_NUMBER = "bga_contract_number";
    private static final String BGA_INDICATOR = "T ";
	
	/**
	 * Constructor
	 */
	public FDFController() {
		super(FDFController.class);
	} 
	
	 /** 
	 * Action method to populate the open FDF parameters that is passed 
	 * to the PDF
	 * @param mapping
	 * 				ActionMapping
	 * @param form
	 * 				Form
	 * @param request
	 * 				HttpServletRequest
	 * @param response
	 * 				HttpServletResponse
	 * @return ActionForward
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	@RequestMapping(method = {RequestMethod.GET,RequestMethod.POST})
	public String doExecute(@Valid @ModelAttribute("dynaForm") DynaForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
			throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}

	    if(logger.isDebugEnabled()) {
		    logger.debug("entry FDFAction -> doExecute");
	    }
	 
	    FDFDocument doc = new FDFDocument();
	    
	    // Get the contract number
	    Contract currentContract = getUserProfile(request).getCurrentContract();
	    int contractNumber = currentContract.getContractNumber();
	    
	    // Add the contract related general parameters
		doc.addField(new FDFField(Constants.CONTRACT_HOLDER, currentContract.getCompanyName()));
		doc.addField(new FDFField(Constants.CONTRACT_NUMBER_8DIGIT, DataUtility.compute8DigitContractNumber(currentContract.getContractNumber())));
		doc.addField(new FDFField(Constants.CONTRACT_NUMBER, Integer.toString(currentContract.getContractNumber())));
		doc.addField(new FDFField(Constants.CONTRACT_CLIENT_ID, currentContract.getClientId()));
        String bgaContractNumber = null;
        if (currentContract.isBundledGaIndicator()) {
            bgaContractNumber = BGA_INDICATOR + contractNumber;
        } else {
            bgaContractNumber = Integer.toString(contractNumber);
        }
        doc.addField(new FDFField(BGA_CONTRACT_NUMBER, bgaContractNumber));

		
		doc.addField(new FDFField(Constants.AS_OF_DATE, getFormattedAsOfDate(currentContract)));

		// Some forms has different parameter name for the company name  
		doc.addField(new FDFField(Constants.SHORT_CONTRACT_NAME, currentContract.getCompanyName()));
	    
	    
	    boolean isAE_ON = false;
	    boolean isACI_ON = false;
	    
	    try {
	    	// Get the AE value from the contract_service_feature table
		    ContractServiceFeature csf = com.manulife.pension.delegate.ContractServiceDelegate
	        .getInstance().getContractServiceFeature(contractNumber,
	                ServiceFeatureConstants.AUTO_ENROLLMENT_FEATURE);
		    
		    String signUpMethod = com.manulife.pension.delegate.ContractServiceDelegate
	        .getInstance().determineSignUpMethod(contractNumber);
		    
		    // Set the AE boolean value to true, if AE is ON
		    if (ContractServiceFeature.internalToBoolean(csf.getValue()).booleanValue()) {
		    	isAE_ON = true;
		    }
		    
		    // Get the ACI value from the contract_service_feature table
		    csf = com.manulife.pension.delegate.ContractServiceDelegate
	        .getInstance().getContractServiceFeature(contractNumber,
	                ServiceFeatureConstants.AUTO_CONTRIBUTION_INCREASE);
		    
		    // Set the ACI boolean value to true, if ACI is ON or DEFAULTED_TO_YES
			if (StringUtils.equals(Constants.ACI_AUTO, signUpMethod)
					|| StringUtils.equals(Constants.ACI_SIGNUP, signUpMethod)) {
				isACI_ON = true;
			}
		    
		} catch (ApplicationException ae) {
	        throw new SystemException(ae.getMessage());
	    }
		
	    // If AE is on, get AE related parameters
	    if (isAE_ON) {
	    	getEZstartFDFOpenParams(doc, contractNumber, request);
	    } 
	    
   	    // If ACI is ON, get the ACI parameters
	    if (isACI_ON) { 
	    	getEZincreaseFDFOpenParams(doc, contractNumber, request);
	    } 
	    
	    // Set the PDF filename if specified
		if (request.getParameter("pdfForm") != null) {
			doc.setPDFFilename((String) request.getParameter("pdfForm"));
		}

    	byte[] bytes = doc.toFDFFormat().getBytes();
    	response.setHeader("Cache-Control", "no-store, no-cache , must-revalidate");
    	response.setHeader("Pragma", "no-cache");
    	response.setHeader("Expires", "Thu, 01 Dec 1994 16:00:00 GMT");
  	  	response.setContentType("application/vnd.fdf");
    	response.getOutputStream().write(bytes);

	    if(logger.isDebugEnabled()) {
		    logger.debug("exit FDFAction <- doExecute");
	    }
	    
	   	return null; 
	}
	
	/**
	 * Retrieves the CSF ACI default values
	 * @param request
	 * 				HttpServletRequest
	 * @param doc
	 * 				FDFDocument
	 * @param currentContract
	 * 				Contract
	 * @throws SystemException
	 */
	private void getEZincreaseFDFOpenParams(
			FDFDocument doc, 
			int contractNumber, 
			HttpServletRequest request) 
	throws SystemException {

		try {
			// Get the ACI feature values
			ContractServiceFeature aciCsf = 
				ContractServiceDelegate.getInstance().getContractServiceFeature(
						contractNumber, ServiceFeatureConstants.AUTO_CONTRIBUTION_INCREASE);
			
			String signUpMethod = com.manulife.pension.delegate.ContractServiceDelegate
		        .getInstance().determineSignUpMethod(contractNumber);
			
			PlanDataLite planData = com.manulife.pension.delegate.ContractServiceDelegate
	        .getInstance().getPlanDataLight(contractNumber);
	
			String increaseRate = "";
			String defaultLimit = "";

			// Populate the ACI values only if the ACI is ON or
			// set to Default To Yes
			if(StringUtils.equals(Constants.ACI_AUTO, signUpMethod)
					|| StringUtils.equals(Constants.ACI_SIGNUP, signUpMethod)) {
	
				// Retrieve the Manage deferral values from the database
				ContractServiceFeature mdCsf = 
					ContractServiceDelegate.getInstance().getContractServiceFeature(
							contractNumber,Constants.MANAGING_DEFERRALS);
 
				// CSF deferral type
				String deferralType = mdCsf.getAttributeValue(Constants.medDeferralType);
				
				// CSF planLimit in dollar value
				String defaultLimitByDollar = 
					aciCsf.getAttributeValue(Constants.aciDefaultDeferralLimitByAmount)!= null ? 
							aciCsf.getAttributeValue(Constants.aciDefaultDeferralLimitByAmount) : "";
				
				// CSF plan limit in percent value
			    String defaultLimitByPercent = "";
				if(StringUtils.equals(Constants.ACI_AUTO, signUpMethod)) {
					defaultLimitByPercent =planData.getAciDefaultAutoIncreaseMaxPercent() != null ?
							String.valueOf(planData.getAciDefaultAutoIncreaseMaxPercent().intValue()) : "";
				} else {
				  defaultLimitByPercent = 
					aciCsf.getAttributeValue(Constants.aciDefaultDeferralLimitByPercent)!= null ? 
							aciCsf.getAttributeValue(Constants.aciDefaultDeferralLimitByPercent) : "";
				}
 
				boolean hasDefaultLimitPercent = false;
				boolean hasdefaultLimitDollar = false;

				if (!"".equals(defaultLimitByDollar)) {
					hasdefaultLimitDollar = true;
				}
				
				if (!"".equals(defaultLimitByPercent)) {
					hasDefaultLimitPercent = true;
				}

				// If the CSF deferral type is dollar retrieve only 
				// the dollar values for the increase rate and plan limit
				if (Constants.DEFERRAL_TYPE_DOLLAR.equals(deferralType)) {

					increaseRate = Constants.DEFERRAL_TYPE_DOLLAR + DataUtility.formatCurrency(
							aciCsf.getAttributeValue(Constants.aciDefaultAnnualIncreaseByAmount));
					
					if (hasdefaultLimitDollar) {
						defaultLimit = Constants.DEFERRAL_TYPE_DOLLAR + defaultLimitByDollar;
					}
				// If the CSF deferral type is percent retrieve only
				// the percent values for the increase rate and plan limit 
				}else if (Constants.DEFERRAL_TYPE_PERCENT.equals(deferralType)) {
					
					if(StringUtils.equals(Constants.ACI_AUTO, signUpMethod)) {
						increaseRate = planData.getAciDefaultIncreasePercent() == null ? ""
								: planData.getAciDefaultIncreasePercent().intValue() + Constants.DEFERRAL_TYPE_PERCENT;
					} else {
						 increaseRate = aciCsf.getAttributeValue(
									Constants.aciDefaultAnnualIncreaseByPercent) + Constants.DEFERRAL_TYPE_PERCENT;
					}
					
					if (hasDefaultLimitPercent) {
						defaultLimit = defaultLimitByPercent + Constants.DEFERRAL_TYPE_PERCENT;
					}
				// If the CSF deferral type is EITHER retrieve both
				// the dollar and percent values for the increase rate and plan limit 
				// separated by "or"
				}else {
					
					// dollar value of Increase rate
					increaseRate = Constants.DEFERRAL_TYPE_DOLLAR + DataUtility.formatCurrency(
							aciCsf.getAttributeValue(Constants.aciDefaultAnnualIncreaseByAmount));
					
					increaseRate = increaseRate + OR_STRING;
					
					// Percent value for the increase rate
					
					if(StringUtils.equals(Constants.ACI_AUTO, signUpMethod)) {
						increaseRate =  increaseRate + ( (planData.getAciDefaultIncreasePercent() == null) ? ""
								: planData.getAciDefaultIncreasePercent().intValue()) + Constants.DEFERRAL_TYPE_PERCENT;
					} else {
						increaseRate = increaseRate + aciCsf.getAttributeValue(
								Constants.aciDefaultAnnualIncreaseByPercent) + Constants.DEFERRAL_TYPE_PERCENT;
					}
					

					// plan limit in dollar
					if (hasdefaultLimitDollar) {
						defaultLimit = Constants.DEFERRAL_TYPE_DOLLAR + defaultLimitByDollar;
					}
					
					if (hasdefaultLimitDollar && hasDefaultLimitPercent) {
						defaultLimit = defaultLimit + OR_STRING;
					}
					
					// plan limit by percent
					if(hasDefaultLimitPercent){
						defaultLimit = defaultLimit + defaultLimitByPercent + Constants.DEFERRAL_TYPE_PERCENT;
					}
				}
				
				// Get the anniversary date
				Calendar anniversaryDate = getContractNextAnniversayDate(
						planData, StringUtils.equals(Constants.ACI_AUTO, signUpMethod) ? true : false);
				String annivDate = SHORT_DATE_FORMAT.format(anniversaryDate.getTime());
 				
				// Add the CSF ACI values to the FDFDocument instance
				doc.addField(new FDFField(Constants.PDF_FIELD_ANNIVERSARY_DATE, annivDate));
				doc.addField(new FDFField(Constants.PDF_FIELD_INCREASE_RATE, increaseRate));
				doc.addField(new FDFField(Constants.PDF_FIELD_PLAN_DEFAULT_MAX, defaultLimit));
				doc.addField(new FDFField(Constants.PDF_FIELD_PLAN_DEFAULT_INCREASE, increaseRate));
				
			}
		} catch(ApplicationException ae) {
			throw new SystemException(ae.getMessage());
		}
	}

	/**
	 * Populates the Auto Enrollment open FDF parameters  
	 * @param request
	 * 				HttpServletRequest
	 * @return FDFDocument
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	private void getEZstartFDFOpenParams(
			FDFDocument doc, 
			int contractNumber, 
			HttpServletRequest request) 
	throws IOException, ServletException, SystemException {
		try {
			// Get the EZstart values from the database
			ContractServiceFeature csf = ContractServiceDelegate.getInstance().getContractServiceFeature(
					contractNumber, ServiceFeatureConstants.AUTO_ENROLLMENT_FEATURE);
			
			ContractServiceFeature mdCsf = ContractServiceDelegate.getInstance().getContractServiceFeature(
					contractNumber, ServiceFeatureConstants.MANAGING_DEFERRALS);
			
			PlanDataLite planData = ContractServiceDelegate.getInstance().getPlanDataLight(contractNumber);
	
			String aef = csf.getValue();
			
			// Check if the EZstart is ON at the CSF 
			if(ContractServiceFeature.internalToBoolean(aef).booleanValue()) {
				// Get the plan entry date from the request
				// passed as a query string from JSP
				String nped = (String)request.getParameter(NEXT_PLAN_ENTRY_DATE_PARAM);
				
				Date applicableNextPlanEntryDate = null;
				if(nped != null) {
					// Parse the anniversary date from String to Date object
					try {
						applicableNextPlanEntryDate = DATE_FORMATTER.parse(nped);
					} catch(ParseException pe) {
						// do nothing  - default to next plan entry date
					}
				}

				// if no next plan entry date is provided by the user then 
				// go with the next one - should never happen
				if(applicableNextPlanEntryDate == null) { 
					applicableNextPlanEntryDate = ContractServiceDelegate.getInstance().getNextPlanEntryDate(
							contractNumber , new Date(System.currentTimeMillis()));
				}
				
				doc.addField(new FDFField(Constants.PDF_FIELD_PLAN_ENTRY_DATE, EmployeeLetterHelper.formatDate(applicableNextPlanEntryDate)));
				doc.addField(new FDFField(Constants.PDF_FIELD_DEFAULT_DEFERRAL_RATE, planData.getDeferralPercentageForAutomaticEnrollment() == null ? "" : planData.getDeferralPercentageForAutomaticEnrollment().intValue()  + "%"));
				doc.addField(new FDFField(Constants.PDF_FIELD_DEFAULT_INVESTMENT_OPTION, EmployeeLetterHelper.getDefaultInvestmentOptions(contractNumber)));
				doc.addField(new FDFField(Constants.PDF_FIELD_OPT_OUT_DATE, EmployeeLetterHelper.calculateOptOutDeadline(applicableNextPlanEntryDate, mdCsf.getAttributeValue(ServiceFeatureConstants.AUTO_ENROLLMENT_OPT_OUT_DEADLINE))));

				// Some of the forms has different parameter names
				doc.addField(new FDFField(Constants.PDF_FIELD_NEXT_PLAN_ENTRY_DATE, EmployeeLetterHelper.formatDate(applicableNextPlanEntryDate)));
				doc.addField(new FDFField(Constants.PDF_FIELD_DEFAULT_DEFERRAL, planData.getDeferralPercentageForAutomaticEnrollment() == null ? "" : planData.getDeferralPercentageForAutomaticEnrollment().intValue() +"%"));
				doc.addField(new FDFField(Constants.PDF_FIELD_DEFAULT_INVESTMENT_OPTIONS, EmployeeLetterHelper.getDefaultInvestmentOptions(contractNumber)));
				doc.addField(new FDFField(Constants.PDF_FIELD_OPT_OUT_DEADLINE, EmployeeLetterHelper.calculateOptOutDeadline(applicableNextPlanEntryDate, mdCsf.getAttributeValue(ServiceFeatureConstants.AUTO_ENROLLMENT_OPT_OUT_DEADLINE))));
			}
		} catch(ApplicationException ae) {
			throw new SystemException(ae.getMessage());
		}	
	}
	
	/**
	 * Formats the contract's as of date to a format MMMM dd, yyyy
	 * @param contract
	 * @return String
	 */
	private String getFormattedAsOfDate(Contract contract) {
		Date asofdate = contract.getContractDates().getAsOfDate();
		return DateRender.formatByPattern(asofdate, "", RenderConstants.EXTRA_LONG_MDY);		
	}
	
	/*
	 * gets the contract anniversary date
	 */
	private Calendar getContractNextAnniversayDate(PlanDataLite planData, boolean isAuto) {
		Calendar contractNextAnniversaryDate = Calendar.getInstance();
		if (isAuto) {
			DayOfYear dayOfYear = planData.getAciAnnualApplyDate();
			if (dayOfYear != null) {
			    contractNextAnniversaryDate.setTime(dayOfYear.getAsDateNonLeapYear());
			}
		} else {
			if (planData.getPlanYearEnd() != null) {
				DayOfYear value = new DayOfYear(planData.getPlanYearEnd().getAsDateNonLeapYear());
				value.incrementDayOfYear(1);
				contractNextAnniversaryDate.setTime(value.getAsDateNonLeapYear());
			}
		}
		return contractNextAnniversaryDate;
	}
	
	
	/**
	 * This code has been changed and added to Validate form and request against
	 * penetration attack, prior to other validations.
	 */
	@Autowired
	   private PSValidatorFWInput  psValidatorFWInput;
	@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	}
	
}