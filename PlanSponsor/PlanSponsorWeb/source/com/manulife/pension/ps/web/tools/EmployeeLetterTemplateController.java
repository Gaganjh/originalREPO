package com.manulife.pension.ps.web.tools;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

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
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.util.FunctionalLogger;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractProfileVO;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.contract.valueobject.DayOfYear;
import com.manulife.pension.service.contract.valueobject.EmployeeVO;
import com.manulife.pension.service.contract.valueobject.ParticipantListVO;
import com.manulife.pension.service.contract.valueobject.ParticipantVO;
import com.manulife.pension.service.plan.valueobject.PlanDataLite;

/**
 * Action class generate the merge file for the word templates
 * related to Auto Enrollment and Auto Contribution Increase * 
 * Modified by Sujith Manikkothu on Oct 20th 2009
 *  	- removed instance variables,"defaultIncreaseByDollar","defaultIncreaseByPercent"
 *  	and "annivDate" to avoid race condition issues(CL:102696)
 * @author Ioana Dogaru
 */

@Controller
@RequestMapping(value ="/participant")

public class EmployeeLetterTemplateController extends PsController{

	
	@ModelAttribute("employeeLetterTemplateAction")
	public  DynaForm populateForm() 
	{
		return new  DynaForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/tools/toolsMenu.jsp");
		}

    private static final int OFFSET_BY_MONTH = 1;
    private static final int OFFSET_BY_YEAR = 2;

    private static final String COMMA = ",";
	private static final String DOUBLE_QUOTES = "\"";
	private static final String SPACE = " ";
	private static final String LINE_BREAK = System.getProperty("line.separator");
		
	private static final BigDecimal ZERO = new BigDecimal("0.000");
	private static final BigDecimal ZERO_DOLLAR = new BigDecimal("0.00");
	
	// Date formats
	private static final DateFormat DATE_FORMATTER = new SimpleDateFormat("MMddyyyy");
	/**
	 * SimpleDateFormat is converted to FastDateFormat to make it thread safe
	 */
	private static final FastDateFormat DATE_FORMATTER1 = FastDateFormat.getInstance("yyyy/MM/dd");
	private static final FastDateFormat SHORT_DATE_FORMAT = FastDateFormat.getInstance("MMMM dd");
	private static final FastDateFormat STANDARD_DATE_FORMAT =  FastDateFormat.getInstance("yyyy-MM-dd");
	private static final FastDateFormat DATE_FORMATTER3 = FastDateFormat.getInstance("MMM dd yyyy");
	
	private static final String CSV_EXTENSION = ".csv";
	private static final String CONTENT_DISPOSITION_TEXT = "Content-Disposition";
	private static final String ATTACHMENT_TEXT = "attachment; filename=";
	private static final String CSV_TEXT = "text/csv";
	
	// column labels for the AE only merge file
	private static final String COLUMN_LABELS_AE = "Name,address 1,address 2,city,state,country,zip," +
			"Short Contract Name,Plan Entry Date," +
			"Default Deferral Rate,Default Investment Option,Opt-Out Date,Contract Number,";

	// common column labels	
	private static final String COLUMN_LABELS_GENERAL = "first name,middle initial,last name,address 1," +
			"address 2,city,state,country,zip,";
	
	// column labels for AE and ACI - Auto 
	private static final String COLUMN_LABELS_AE_ACI_AUTO = 
		COLUMN_LABELS_GENERAL + "short contract name,contract number,plan entry date," +
				"opt out date,default deferral rate,default investment option,anniversary date," +
				"$x or x%,plan default maximum";
	
	// column labels for AE and ACI - Manual
	private static final String COLUMN_LABELS_AE_ACI_MANUAL = COLUMN_LABELS_AE_ACI_AUTO + ",plan default increase";
	
	// column labels for ACI - Auto
	private static final String COLUMN_LABELS_ACI_AUTO = COLUMN_LABELS_GENERAL + "short contract name," +
			"contract number,anniversary date,$x or x%,plan default maximum";
	
	// column labels for ACI - Manual
	private static final String COLUMN_LABELS_ACI_MANUAL = COLUMN_LABELS_ACI_AUTO + ",plan default increase";
	
	// column labels for Upcoming Anniversary
	private static final String COLUMN_LABELS_UPCOMING_ANNIVERSARY = 
		COLUMN_LABELS_GENERAL + "anniversary_date,$x or x%";
	// column labels for EC
	
	private static final String COLUMN_LABELS_EC = COLUMN_LABELS_GENERAL + "Short Contract Name," +
			"Contract Number,Plan Entry Date";
	
	// Constants for the option selected in the JSP
	private static final String AE_ONLY = "ae";
	private static final String AE_AND_ACI = "aeACI";
	private static final String ACI_ONLY = "aci";
	private static final String EC = "EC";
	private static final String UPCOMING_ANNIVERSARY = "upCommingAnniv";
	
	// if the Upcoming anniversary merge file is selected, 
	// then the anniversary date should be sorted in ascending order
	private static String SORT_ORDER = "15 ASC";

	private static final String OUTSTANDING_PROCESS_CODE = "PA";
	private static final String ACI_CONTRIBUTION_INSTRN_SRC_CODE = "CI";
	
	private static final String ANNIVERSARY_DATE = "annivDate";
	
	/**
	 * Empty constructor.
	 */
	public EmployeeLetterTemplateController() {
		super(EmployeeLetterTemplateController.class);
	} 

	/**
	 * Method to handle the request and generate the merge file for templates
	 * in Tools Page
	 * 
	 * @param mapping
	 * 				ActionMapping
	 * @param form
	 * 				Form 
	 * @param request
	 * 				HttpServletRequest 
	 * @param response
	 * 				HttpServletResponse
	 * 
	 * @return ActionForward
	 * 
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	
	@RequestMapping(value ="/employeeLetterDownload/", params= {"task=download"},  method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doExecute(@Valid @ModelAttribute("employeeLetterTemplateAction") DynaForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
	
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        }  
		
		
	    if(logger.isDebugEnabled()) {
		    logger.debug("entry EmployeeLetterTemplateAction -> doExecute");
	    }
	 
	    // Get the service feature value from request
		String serviceFeature = (String)request.getParameter("sf");
		
		// If the upcoming anniversary merge file is requested, 
		// then set the boolean value to true,
		// else set NULL to the sort order
		boolean isUpComingAnnivMergeFile = false;
		if (UPCOMING_ANNIVERSARY.equals(serviceFeature)) {
			isUpComingAnnivMergeFile = true;
		}
		
		// if the ACi signup method is auto set the boolean to TRUE
		Object isAuto = request.getSession().getAttribute(Constants.SIGNUP_METHOD);
		boolean isAciSignUpMethodAuto = new Boolean(
				((String)(isAuto != null && Constants.ACI_AUTO.equals(isAuto)? "true" : "false"))).booleanValue();
		
		Contract currentContract = getUserProfile(request).getCurrentContract();

		if (AE_ONLY.equals(serviceFeature)){
			
			getEmployeeMergeFileForAe(request, response);
			
		}else if (AE_AND_ACI.equals(serviceFeature)){
			
			getEmployeeMergeFileForAeAci(request, response, currentContract, isAciSignUpMethodAuto);
	        FunctionalLogger.INSTANCE.log("Download Eligible Employee File", request, getClass(), "doExecute");

		}else if (ACI_ONLY.equals(serviceFeature)){
			
			getParticipantMergeFileForAci(request, response, currentContract, 
					isAciSignUpMethodAuto);
			
		}else if (UPCOMING_ANNIVERSARY.equals(serviceFeature)) {
			getParticipantMergeFileForUpcomigAnniversary(request, response, currentContract);
			
		}else if (EC.equals(serviceFeature)){
			
			getEmployeeMergeFileForEC(request, response);
			
		}
		
	    if(logger.isDebugEnabled()) {
		    logger.debug("exit EmployeeLetterTemplateAction <- doExecute");
	    }
	    
	    // Since the data is stream out, return null
	  	return null; 
    }
	
	/**
	 * Method generates the participant merge file in .csv format
	 * and puts in the output stream for ACI templates
	 * 
	 * @param request
	 * 				HttpServletRequest
	 * @param response
	 * 				HttpServletResponse
	 * @param currentContract
	 * 				Contract
	 * @param isAciSignUpMethodAuto
	 * 				boolean
	 * 
	 * @throws SystemException
	 * @throws ServletException
	 * @throws IOException
	 * @throws ApplicationException
	 */
	public void getParticipantMergeFileForUpcomigAnniversary(
			HttpServletRequest request, 
			HttpServletResponse response,
			Contract currentContract) throws SystemException, ServletException, IOException {
		
		ParticipantListVO pListVo = 
			ContractServiceDelegate.getInstance().getParticipantList(
					currentContract.getContractNumber(), SORT_ORDER);
		
		PlanDataLite planData = ContractServiceDelegate.getInstance().getPlanDataLight(currentContract.getContractNumber());
		
		String signUp = ContractServiceDelegate.getInstance().determineSignUpMethod(currentContract.getContractNumber());
		boolean isAuto = false;
		if(StringUtils.equals("A", signUp)) {
			isAuto = true;
		}
		
		List<ParticipantVO> mostRecentEzIncreaseContributionChangeRequest = ContractServiceDelegate.getInstance()
		.getMostRecentEzIncreaseContributionChangeRequest(currentContract.getContractNumber());
		
		StringBuffer buffer = new StringBuffer();
	    
		buffer = new StringBuffer(COLUMN_LABELS_UPCOMING_ANNIVERSARY);
		buffer.append(LINE_BREAK);

            Collection pCollection = pListVo.getParticipants();
            if (pCollection != null){
                Iterator particaipantListIterator = pCollection.iterator();
                
                while(particaipantListIterator.hasNext()){
                    
                    ParticipantVO pVo = (ParticipantVO) particaipantListIterator.next();
                    Date participantAnniversaryDate = pVo.getAciAnniversaryDate(); 
                
                    // If the PPT has not turned ON the EZincrease service and
                    // if the PPT's anniversary date is not available and  
                    // if the PPT's anniversary date is not within the next 90 days, 
                    //      then just continue the loop, no need to include this PPT
                    
                    //CL119569 fix - Mail Merge File scheduledDeferral EZI incorrect - start
                    try{
                        if ("Y".equals(pVo.getAciSettingInd()) && 
                                (participantAnniversaryDate != null &&
                                 isAnniversaryWithinNext90Days(participantAnniversaryDate))) {
    
                            buffer.append(getParticipantGreetings(pVo, request)); // gets 1 to 9
                            buffer.append(getParticipantNextAnniversayDate(currentContract
                                    .getContactId(), pVo.getProfileId(), pVo
                                    .getAciAnniversaryDate(), planData, isAuto,
                                    mostRecentEzIncreaseContributionChangeRequest)); // 10  anniversary  date
                            buffer.append(COMMA);
                            buffer.append(determineIncreaseAmountOrRate(pVo, currentContract.getContractNumber()));
                            buffer.append(LINE_BREAK);
    
                        }else
                            continue;
                        
                    } catch (ParseException pe) {
                        logger.error("Anniversary date could not be parsed for the PPT: conractId="+currentContract.getContractNumber()+", ProfileId="+pVo.getProfileId()+", Anniversary Date="+participantAnniversaryDate, pe );
                        throw new SystemException(pe.getMessage());
                    }
                    //CL119569 fix - Mail Merge File scheduledDeferral EZI incorrect - end
                }
            }
            
            streamDownloadData(request, response, CSV_TEXT,
                    getFileName(request, false, true), buffer.toString().getBytes());
            
        
	}
	
	/**
	 * Method generates the participant merge file in .csv format
	 * and puts in the output stream for ACI templates
	 * 
	 * @param request
	 * 				HttpServletRequest
	 * @param response
	 * 				HttpServletResponse
	 * @param currentContract
	 * 				Contract
	 * @param isAciSignUpMethodAuto
	 * 				boolean
	 * 
	 * @throws SystemException
	 * @throws ServletException
	 * @throws IOException
	 */
	public void getParticipantMergeFileForAci(
			HttpServletRequest request, 
			HttpServletResponse response,
			Contract currentContract, 
			boolean isAciSignUpMethodAuto) throws SystemException, ServletException, IOException {
		
		ParticipantListVO pListVo = 
			ContractServiceDelegate.getInstance().getParticipantList(currentContract.getContractNumber(), null);
		PlanDataLite planData = ContractServiceDelegate.getInstance().getPlanDataLight(currentContract.getContractNumber());
		
		String signUp = ContractServiceDelegate.getInstance().determineSignUpMethod(currentContract.getContractNumber());
		boolean isAuto = false;
		if(StringUtils.equals("A", signUp)) {
			isAuto = true;
		}
		
		ContractServiceFeature aciCsf;
		ContractServiceFeature mdCsf;
		try {
			aciCsf = ContractServiceDelegate.getInstance()
			.getContractServiceFeature(currentContract.getContractNumber(), ServiceFeatureConstants.AUTO_CONTRIBUTION_INCREASE);
			
			mdCsf = ContractServiceDelegate.getInstance()
			.getContractServiceFeature(currentContract.getContractNumber(), ServiceFeatureConstants.MANAGING_DEFERRALS);
		} catch(ApplicationException ae){
			throw new SystemException(ae.getMessage());
		}
		
		List<ParticipantVO> mostRecentEzIncreaseContributionChangeRequest = ContractServiceDelegate.getInstance()
		.getMostRecentEzIncreaseContributionChangeRequest(currentContract.getContractNumber());
		
		StringBuffer buffer = new StringBuffer();
		StringBuffer finalBuffer = new StringBuffer();
	    
	    finalBuffer = new StringBuffer(COLUMN_LABELS_ACI_MANUAL);

	    finalBuffer.append(LINE_BREAK);
	    
		Collection pCollection = pListVo.getParticipants();
		if (pCollection != null){
			
			Iterator particaipantListIterator = pCollection.iterator();
			
			while(particaipantListIterator.hasNext()){
				
				ParticipantVO pVo = (ParticipantVO) particaipantListIterator.next();
							
				if ("Y".equals(pVo.getAutoEnrollOptOutInd()) || 
				   (isAciSignUpMethodAuto && !"Y".equals(pVo.getAciSettingInd())) ||
				   (!isAciSignUpMethodAuto && pVo.getAciSettingInd() != null && !"".equals(pVo.getAciSettingInd().trim())))
					continue;
				
				buffer.append(getParticipantGreetings(pVo, request)); // gets 1 to 9
				
				buffer.append(DOUBLE_QUOTES); // 10 short contract name
			    buffer.append(currentContract.getCompanyName() == null ? "" :currentContract.getCompanyName().trim()); // short contract name
			    buffer.append(DOUBLE_QUOTES);
			    
			    buffer.append(COMMA);
			    buffer.append(DOUBLE_QUOTES); // 11 contract number
			    buffer.append(currentContract.getContractNumber());
			    buffer.append(DOUBLE_QUOTES);
			    
			    String deferIncAmt = pVo.getDeferIncAmt();
			    String deferIncPct = pVo.getDeferIncPct();
			    String deferInc = "";
			    boolean hasIncreaseRateByDollar = false;
			    boolean hasIncreaseRateByPercent = false;
			    Date parAnnivDate = pVo.getAciAnniversaryDate();
			    
			    // To determine the PPT's increase rate
			    if (deferIncAmt != null && !"".equals(deferIncAmt) && (ZERO_DOLLAR).compareTo(new BigDecimal(deferIncAmt)) != 0){
			    	deferInc = "$" + DataUtility.formatValueWithoutTrailingZeros(deferIncAmt);
			    	hasIncreaseRateByDollar = true;
			    }
			    if (deferIncPct != null && !"".equals(deferIncPct) && (ZERO).compareTo(new BigDecimal(deferIncPct)) != 0){
			    	if (hasIncreaseRateByDollar){
			    		deferInc = " or " + DataUtility.formatValueWithoutTrailingZeros(deferIncPct) + "%";
			    	}else
			    		deferInc = DataUtility.formatValueWithoutTrailingZeros(deferIncPct) + "%";
			    	hasIncreaseRateByPercent = true;
			    }
			    
			    StringBuffer csfACIDefaultValues =new StringBuffer();
			    if (hasIncreaseRateByDollar || hasIncreaseRateByPercent){
			    	csfACIDefaultValues.append(COMMA);
			    	csfACIDefaultValues.append(DOUBLE_QUOTES); // 13 defer increase rate by percent
			    	csfACIDefaultValues.append(deferInc); 
			    	csfACIDefaultValues.append(DOUBLE_QUOTES);
			    	csfACIDefaultValues.append(getCSFAci(currentContract, false, true,request,aciCsf, planData, signUp, mdCsf)); // 14 plan default limit 
			    }else {
			    	csfACIDefaultValues.append(getCSFAci(currentContract, true, true,request,aciCsf, planData, signUp, mdCsf));  // 13,14 defer increase rate and plan default Limit 
			    }
			    
			    buffer.append(COMMA);
				buffer.append(DOUBLE_QUOTES);
				buffer.append(getParticipantNextAnniversayDate(currentContract
						.getContactId(), pVo.getProfileId(), pVo
						.getAciAnniversaryDate(), planData, isAuto,
						mostRecentEzIncreaseContributionChangeRequest)); // 12 anniversary date
			    buffer.append(DOUBLE_QUOTES);
			    
			    buffer.append(csfACIDefaultValues);
			    
			    // if the ACI signup method is manual add the default incRate
			    buffer.append(getCSFAci(currentContract, true,false,request,aciCsf, planData, signUp, mdCsf));  // 15 default defer increase rate
			    
			    buffer.append(LINE_BREAK);
			}
			
			finalBuffer.append(buffer);
		}
		
		streamDownloadData(request, response, CSV_TEXT,
				getFileName(request, true, false), finalBuffer.toString().getBytes());
	}
	
	/**
	 * Method generates the Employee merge file in .csv format
	 * for the AE and ACI word template and puts in the output stream
	 * 
	 * @param request
	 * 				HttpServletRequest
	 * @param response
	 * 				HttpServletResponse
	 * @param currentContract
	 * 				Contract
	 * @param isAciSignUpMethodAuto
	 * 				boolean
	 * 
	 * @throws SystemException
	 * @throws ServletException
	 * @throws IOException
	 */
	public void getEmployeeMergeFileForAeAci(
			HttpServletRequest request, 
			HttpServletResponse response,
			Contract currentContract,
			boolean isAciSignUpMethodAuto) throws SystemException, ServletException, IOException {
		
		try{
			ContractServiceFeature csf = ContractServiceDelegate.getInstance()
						.getContractServiceFeature(currentContract.getContractNumber(), 
								ServiceFeatureConstants.AUTO_ENROLLMENT_FEATURE);
			
			ContractServiceFeature mdCsf = ContractServiceDelegate.getInstance()
			.getContractServiceFeature(currentContract.getContractNumber(), 
					ServiceFeatureConstants.MANAGING_DEFERRALS);
			
			PlanDataLite planData = ContractServiceDelegate.getInstance().getPlanDataLight(currentContract.getContractNumber());
			
			String signUp = ContractServiceDelegate.getInstance().determineSignUpMethod(currentContract.getContractNumber());
			boolean isAuto = false;
			if(StringUtils.equals("A", signUp)) {
				isAuto = true;
			}
			
			List<ParticipantVO> mostRecentEzIncreaseContributionChangeRequest = ContractServiceDelegate.getInstance()
					.getMostRecentEzIncreaseContributionChangeRequest(currentContract.getContractNumber());
			
			StringBuffer buffer = new StringBuffer();
			
			StringBuffer finalBuffer = new StringBuffer();
			
			finalBuffer.append(COLUMN_LABELS_AE_ACI_MANUAL);
			
			finalBuffer.append(LINE_BREAK);
			
			String aef = csf.getValue();
			if(ContractServiceFeature.internalToBoolean(aef).booleanValue())
			{
				String nped = (String)request.getParameter("nped");
				Date applicableNextPlanEntryDate = null;
				if(nped != null)
				{
					try{
						applicableNextPlanEntryDate = DATE_FORMATTER1.parse(nped);
					}catch(ParseException pe)
					{
						// do nothing  - default to next plan entry date
					}
				}
				
				// if no next plan entry date is provided by the user then go with the next one - should never happen
				if(applicableNextPlanEntryDate == null)
					applicableNextPlanEntryDate = com.manulife.pension.delegate.ContractServiceDelegate.getInstance().getNextPlanEntryDate(currentContract.getContractNumber(), new Date(System.currentTimeMillis()));
				
				Date initialEnrollmentDate = null;
				
				try{
					initialEnrollmentDate = ContractServiceFeature.internalToDate(csf.getAttributeValue(ServiceFeatureConstants.AUTO_ENROLLMENT_INITIAL_ENROLLMENT_DATE));
				}catch(ParseException pe)
				{
					// do nothing initialEnrollmentDate will stay null
				}
			    
				String frequency = getPlanFrequency(currentContract.getContractNumber());
				
				if(applicableNextPlanEntryDate == null || initialEnrollmentDate == null || frequency == null || frequency.trim().length() == 0)
					throw new SystemException("Contract service feature attributes not set properly for contract " + currentContract.getContractNumber());
				
			    Date previousPED = EmployeeLetterHelper.calculatePreviousPED(applicableNextPlanEntryDate, initialEnrollmentDate, frequency);
				
			    StringBuffer aeInfo = new StringBuffer();
			    
			    aeInfo.append(COMMA);
			    aeInfo.append(DOUBLE_QUOTES);// 12 plan entry date
			    aeInfo.append(EmployeeLetterHelper.formatDate(applicableNextPlanEntryDate)); 
			    aeInfo.append(DOUBLE_QUOTES);
			    aeInfo.append(COMMA);
			    aeInfo.append(DOUBLE_QUOTES); // 13 opt-out deadline
			    aeInfo.append(EmployeeLetterHelper.calculateOptOutDeadline(applicableNextPlanEntryDate, mdCsf.getAttributeValue(ServiceFeatureConstants.AUTO_ENROLLMENT_OPT_OUT_DEADLINE)));
			    aeInfo.append(DOUBLE_QUOTES);
			    aeInfo.append(COMMA);
			    aeInfo.append(DOUBLE_QUOTES); // 14 default deferral rate 
			    aeInfo.append(planData.getDeferralPercentageForAutomaticEnrollment() == null ? "" :  planData.getDeferralPercentageForAutomaticEnrollment().intValue() + "%");
			    aeInfo.append(DOUBLE_QUOTES);
				aeInfo.append(COMMA);
				aeInfo.append(DOUBLE_QUOTES); // 15 default investment option
				aeInfo.append(EmployeeLetterHelper.getDefaultInvestmentOptions(currentContract.getContractNumber()));
				aeInfo.append(DOUBLE_QUOTES);
				
			    List eligibleEmployees = ContractServiceDelegate.getInstance()
			    		.getAllEmployeesPendingEnrollment(currentContract.getContractNumber(),applicableNextPlanEntryDate, previousPED);
			    
				ContractServiceFeature aciCsf = ContractServiceDelegate.getInstance()
						.getContractServiceFeature(currentContract.getContractNumber(), ServiceFeatureConstants.AUTO_CONTRIBUTION_INCREASE);

			    if(eligibleEmployees != null)
				{
					Iterator eeIt = eligibleEmployees.iterator();
										
					while(eeIt.hasNext())
					{
						EmployeeVO e = (EmployeeVO)eeIt.next();
						buffer.append(DOUBLE_QUOTES); // 1 first name 
						buffer.append(e.getFirstName() == null ? "" : e.getFirstName());
						buffer.append(DOUBLE_QUOTES);
						buffer.append(COMMA);
						buffer.append(DOUBLE_QUOTES); // 2 initial
						if(e.getMiddleInitial() != null && e.getMiddleInitial().trim().length() > 0 )
							buffer.append(e.getMiddleInitial()); 
						else 
							buffer.append("");
						buffer.append(DOUBLE_QUOTES);
						buffer.append(COMMA);
						buffer.append(DOUBLE_QUOTES); // 2 initial
						buffer.append(e.getLastName() == null ? "" : e.getLastName()); //3 last name 
						buffer.append(DOUBLE_QUOTES);
						buffer.append(COMMA);
						if (checkEmployeeAddressAvailable(e)){
							
							buffer.append(DOUBLE_QUOTES); // 4 address 1 
							buffer.append(e.getAddressLine1() == null ? "" : e.getAddressLine1()); 
							buffer.append(DOUBLE_QUOTES);
							buffer.append(COMMA);
							buffer.append(DOUBLE_QUOTES); // 5 address 2
							buffer.append(e.getAddressLine2() == null ? "" : e.getAddressLine2());
							buffer.append(DOUBLE_QUOTES);
							buffer.append(COMMA);
							buffer.append(DOUBLE_QUOTES); // 6 city
							buffer.append(e.getCityName() == null ? "" : e.getCityName());
							buffer.append(DOUBLE_QUOTES);
							buffer.append(COMMA);
							buffer.append(DOUBLE_QUOTES); // 7 state
							buffer.append(e.getStateCode() == null ? "" : e.getStateCode());
							buffer.append(DOUBLE_QUOTES);
							buffer.append(COMMA);
							buffer.append(DOUBLE_QUOTES); // 8 country
							buffer.append(e.getCountryName() == null ? "" : e.getCountryName());
							buffer.append(DOUBLE_QUOTES);
							buffer.append(COMMA);
							buffer.append(DOUBLE_QUOTES); // 9 zip
							buffer.append(e.getZipCode() == null ? "" : e.getZipCode());
							buffer.append(DOUBLE_QUOTES);
							buffer.append(COMMA);
						}else {
							buffer.append(getContractAddress(request));
						}
						buffer.append(DOUBLE_QUOTES); // 10 short contract name
					    buffer.append(currentContract.getCompanyName() == null ? "" :currentContract.getCompanyName().trim()); // short contract name
					    buffer.append(DOUBLE_QUOTES);
					    buffer.append(COMMA);
						buffer.append(DOUBLE_QUOTES); // 11 contract number
					    buffer.append(currentContract.getContractNumber()); // contract number
					    buffer.append(DOUBLE_QUOTES);
						buffer.append(aeInfo); // 12 to 15
						buffer.append(COMMA);
						
						StringBuffer csfACIDefaultValue = getCSFAci(currentContract, true, true,request, aciCsf, planData, signUp, mdCsf);
						
						buffer.append(DOUBLE_QUOTES);
						buffer
								.append(getParticipantNextAnniversayDate(
										currentContract.getContactId(),
										e.getProfileId(),
										null,
										planData,
										isAuto,
										mostRecentEzIncreaseContributionChangeRequest)); // 16 anniversary date
						
						buffer.append(DOUBLE_QUOTES);
						
						buffer.append(csfACIDefaultValue); // 17 & 18 \\ Increase rate and Plan limit
						
						buffer.append(getCSFAci(currentContract, true,false,request, aciCsf, planData, signUp, mdCsf));  // 21 default defer increase rate
						
						buffer.append(LINE_BREAK);
					}
					finalBuffer.append(buffer);
				}
				
				streamDownloadData(request, response, CSV_TEXT,
						getFileName(request, false, false), finalBuffer.toString().getBytes());
			}
		}catch(ApplicationException ae){
			throw new SystemException(ae.getMessage());
		}
	}

	/**
	 * Method generates the Employee merge file in .csv format
	 * for the AE word template and puts in the output stream
	 * 
	 * @param request
	 * 				HttpServletRequest
	 * @param response
	 * 				HttpServletResponse
	 * @param currentContract
	 * 				Contract
	 * @param isAciSignUpMethodAuto
	 * 				boolean
	 * 
	 * @throws SystemException
	 * @throws ServletException
	 * @throws IOException
	 */
	public void getEmployeeMergeFileForAe(
			HttpServletRequest request, 
			HttpServletResponse response) throws SystemException, ServletException,IOException{
		
		Contract currentContract = getUserProfile(request).getCurrentContract();
		
		try{
			ContractServiceFeature csf = com.manulife.pension.delegate.ContractServiceDelegate.getInstance().getContractServiceFeature(currentContract.getContractNumber(), ServiceFeatureConstants.AUTO_ENROLLMENT_FEATURE);
			ContractServiceFeature mdCsf = com.manulife.pension.delegate.ContractServiceDelegate.getInstance().getContractServiceFeature(currentContract.getContractNumber(), ServiceFeatureConstants.MANAGING_DEFERRALS);
			PlanDataLite planData = com.manulife.pension.delegate.ContractServiceDelegate.getInstance().getPlanDataLight(currentContract.getContractNumber());
			
			StringBuffer buffer = new StringBuffer(COLUMN_LABELS_AE);
			buffer.append(LINE_BREAK);
			
			String aef = csf.getValue();
			if(ContractServiceFeature.internalToBoolean(aef).booleanValue())
			{
				String nped = (String)request.getParameter("nped");
				Date applicableNextPlanEntryDate = null;
				if(nped != null)
				{
					try{
						applicableNextPlanEntryDate = DATE_FORMATTER1.parse(nped);
					}catch(ParseException pe)
					{
						// do nothing  - default to next plan entry date
					}
				}
				
				// if no next plan entry date is provided by the user then go with the next one - should never happen
				if(applicableNextPlanEntryDate == null)
					applicableNextPlanEntryDate = com.manulife.pension.delegate.ContractServiceDelegate.getInstance().getNextPlanEntryDate(currentContract.getContractNumber(), new Date(System.currentTimeMillis()));
				
				Date initialEnrollmentDate = null;
				
				try{
					initialEnrollmentDate = ContractServiceFeature.internalToDate(csf.getAttributeValue(ServiceFeatureConstants.AUTO_ENROLLMENT_INITIAL_ENROLLMENT_DATE));
				}catch(ParseException pe)
				{
					// do nothing initialEnrollmentDate will stay null
				}
			    
				// String frequency = csf.getAttributeValue(ServiceFeatureConstants.AUTO_ENROLLMENT_PLAN_ENTRY_FREQUENCY);
				String frequency = getPlanFrequency(currentContract.getContractNumber());
				
				if(applicableNextPlanEntryDate == null || initialEnrollmentDate == null || frequency == null || frequency.trim().length() == 0)
					throw new SystemException("Contract service feature attributes not set properly for contract " + currentContract.getContractNumber());
				
			    Date previousPED = EmployeeLetterHelper.calculatePreviousPED(applicableNextPlanEntryDate, initialEnrollmentDate, frequency);
			    
				StringBuffer csfInfo1 = new StringBuffer();
				csfInfo1.append(DOUBLE_QUOTES);
				csfInfo1.append(currentContract.getCompanyName() == null ? "" :currentContract.getCompanyName().trim());
				csfInfo1.append(DOUBLE_QUOTES);
				csfInfo1.append(COMMA);
				csfInfo1.append(DOUBLE_QUOTES);
				csfInfo1.append(EmployeeLetterHelper.formatDate(applicableNextPlanEntryDate));
				csfInfo1.append(DOUBLE_QUOTES);
				csfInfo1.append(COMMA);
				
				StringBuffer csfInfo2 = new StringBuffer();
				csfInfo2.append(COMMA);
				csfInfo2.append(DOUBLE_QUOTES);
				csfInfo2.append(EmployeeLetterHelper.getDefaultInvestmentOptions(currentContract.getContractNumber()));
				csfInfo2.append(DOUBLE_QUOTES);
				csfInfo2.append(COMMA);
				csfInfo2.append(DOUBLE_QUOTES);
				csfInfo2.append(EmployeeLetterHelper.calculateOptOutDeadline(applicableNextPlanEntryDate, mdCsf.getAttributeValue(ServiceFeatureConstants.AUTO_ENROLLMENT_OPT_OUT_DEADLINE)));
				csfInfo2.append(DOUBLE_QUOTES);
				csfInfo2.append(COMMA);
				csfInfo2.append(currentContract.getContractNumber());
				csfInfo2.append(COMMA);
				csfInfo2.append(LINE_BREAK);
				
				List eligibleEmployees = ContractServiceDelegate.getInstance()
						.getAllEmployeesPendingEnrollment(currentContract.getContractNumber(),
								applicableNextPlanEntryDate, previousPED);
				
				if(eligibleEmployees != null)
				{
					Iterator eeIt = eligibleEmployees.iterator();
										
					while(eeIt.hasNext())
					{
						EmployeeVO e = (EmployeeVO)eeIt.next();
						
						buffer.append(DOUBLE_QUOTES);
						buffer.append(e.getFirstName() == null ? "" : e.getFirstName());
						if(e.getMiddleInitial() != null && e.getMiddleInitial().trim().length() > 0 )
						{
							buffer.append(SPACE);
							buffer.append(e.getMiddleInitial());
						}
						buffer.append(SPACE);
						buffer.append(e.getLastName() == null ? "" : e.getLastName());
						buffer.append(DOUBLE_QUOTES);
						buffer.append(COMMA);
						if (checkEmployeeAddressAvailable(e)){
							
							buffer.append(DOUBLE_QUOTES); //  address 1 
							buffer.append(e.getAddressLine1() == null ? "" : e.getAddressLine1()); 
							buffer.append(DOUBLE_QUOTES);
							buffer.append(COMMA);
							buffer.append(DOUBLE_QUOTES); //  address 2
							buffer.append(e.getAddressLine2() == null ? "" : e.getAddressLine2());
							buffer.append(DOUBLE_QUOTES);
							buffer.append(COMMA);
							buffer.append(DOUBLE_QUOTES); //  city
							buffer.append(e.getCityName() == null ? "" : e.getCityName());
							buffer.append(DOUBLE_QUOTES);
							buffer.append(COMMA);
							buffer.append(DOUBLE_QUOTES); //  state
							buffer.append(e.getStateCode() == null ? "" : e.getStateCode());
							buffer.append(DOUBLE_QUOTES);
							buffer.append(COMMA);
							buffer.append(DOUBLE_QUOTES); //  country
							buffer.append(e.getCountryName() == null ? "" : e.getCountryName());
							buffer.append(DOUBLE_QUOTES);
							buffer.append(COMMA);
							buffer.append(DOUBLE_QUOTES); //  zip
							buffer.append(e.getZipCode() == null ? "" : e.getZipCode());
							buffer.append(DOUBLE_QUOTES);
							buffer.append(COMMA);
							
						}else {
							buffer.append(getContractAddress(request));
						}
						buffer.append(csfInfo1);
						
						/* if(e.getBeforeTaxDeferralPct() != null && e.getBeforeTaxDeferralPct().trim().length() > 0)
						{
							buffer.append(e.getBeforeTaxDeferralPct());
						}
						else
						{ */
							buffer.append(planData.getDeferralPercentageForAutomaticEnrollment() == null ? "" : planData.getDeferralPercentageForAutomaticEnrollment().intValue() +"%");
						//}
						
						buffer.append(csfInfo2);
					}
				}
								
				streamDownloadData(request, response, CSV_TEXT,
							getFileName(request, false, false), buffer.toString().getBytes());
			}
		}catch(ApplicationException ae) {
			throw new SystemException(ae.getMessage());
		}
	}

	/**
	 * Method gets the participant's first name, initial, last name
	 * address
	 * 
	 * @param currentContract
	 * 				Contract
	 * @param pVo
	 * 				ParticipantVO
	 * 
	 * @return StringBuffer
	 * 
	 * @throws SystemException
	 */
	private StringBuffer getParticipantGreetings(
			ParticipantVO pVo, 
			HttpServletRequest request) throws ServletException, SystemException, IOException {
		
		StringBuffer buffer = new StringBuffer();
		
		buffer.append(DOUBLE_QUOTES); // 1 first name 
		buffer.append(pVo.getFirstName() == null ? "" : pVo.getFirstName());
		buffer.append(DOUBLE_QUOTES);
		buffer.append(COMMA);
		buffer.append(DOUBLE_QUOTES); // 2 initial
		if(pVo.getMiddleInitial() != null && pVo.getMiddleInitial().trim().length() > 0 )
			buffer.append(pVo.getMiddleInitial()); 
		else 
			buffer.append("");
		buffer.append(DOUBLE_QUOTES);
		buffer.append(COMMA);
		buffer.append(DOUBLE_QUOTES); // 2 initial
		buffer.append(pVo.getLastName() == null ? "" : pVo.getLastName()); //3 last name 
		buffer.append(DOUBLE_QUOTES);
		buffer.append(COMMA);
		
		// If participant address is not available then get the contract address
		if (checkParticipantAddressAvailable(pVo)){
			buffer.append(DOUBLE_QUOTES); // 4 address 1 
			buffer.append(pVo.getAddressLine1() == null ? "" : pVo.getAddressLine1()); 
			buffer.append(DOUBLE_QUOTES);
			buffer.append(COMMA);
			buffer.append(DOUBLE_QUOTES); // 5 address 2
			buffer.append(pVo.getAddressLine2() == null ? "" : pVo.getAddressLine2());
			buffer.append(DOUBLE_QUOTES);
			buffer.append(COMMA);
			buffer.append(DOUBLE_QUOTES); // 6 city
			buffer.append(pVo.getCityName() == null ? "" : pVo.getCityName());
			buffer.append(DOUBLE_QUOTES);
			buffer.append(COMMA);
			buffer.append(DOUBLE_QUOTES); // 7 state
			buffer.append(pVo.getStateCode() == null ? "" : pVo.getStateCode());
			buffer.append(DOUBLE_QUOTES);
			buffer.append(COMMA);
			buffer.append(DOUBLE_QUOTES); // 8 country
			buffer.append(pVo.getCountryName() == null ? "" : pVo.getCountryName());
			buffer.append(DOUBLE_QUOTES);
			buffer.append(COMMA);
			buffer.append(DOUBLE_QUOTES); // 9 zip
			buffer.append(pVo.getZipCode() == null ? "" : pVo.getZipCode());
			buffer.append(DOUBLE_QUOTES);
			buffer.append(COMMA);
		}else {
			buffer.append(getContractAddress(request));
		}
		
		return buffer;
	}
	
	/**
	 * Returns the Contract default increase rate and plan limit 
	 * as StringBuffer in .csv format
	 *  
	 * @param currentContract
	 * 					Contract
	 * @param includeIncreaseRate
	 * 					boolean
	 * @param includePlanDefaultMaxLimit
	 * 					boolean
	 * @param request
	 * 					HttpServletRequest
	 * @param signUp 
	 * @param planData 
	 * @param aciCsf 
	 * @return StringBuffer
	 * 
	 * @throws SystemException
	 */
	public StringBuffer getCSFAci(Contract currentContract,
			boolean includeIncreaseRate, boolean includePlanDefaultMaxLimit,
			HttpServletRequest request, ContractServiceFeature aciCsf,
			PlanDataLite planData, String signUpMethod, ContractServiceFeature mdCsf) throws SystemException {
		
			StringBuffer aciCsfInfo = new StringBuffer();
			
			if (StringUtils.equals(Constants.ACI_AUTO, signUpMethod)
					|| StringUtils.equals(Constants.ACI_SIGNUP, signUpMethod)) {
				 
				String deferralType = mdCsf.getAttributeValue(Constants.medDeferralType);
				String defaultLimitByDollar = aciCsf.getAttributeValue(Constants.aciDefaultDeferralLimitByAmount)!= null ?
						 aciCsf.getAttributeValue(Constants.aciDefaultDeferralLimitByAmount) : "";
				String defaultLimitByPercent = "";
				
				if (StringUtils.equals(Constants.ACI_AUTO, signUpMethod)) {
					defaultLimitByPercent = (planData.getAciDefaultAutoIncreaseMaxPercent() != null && planData.getAciDefaultAutoIncreaseMaxPercent().intValue() != 0) ?
					       String.valueOf(planData.getAciDefaultAutoIncreaseMaxPercent().intValue()) : "";
				} else {
					defaultLimitByPercent = aciCsf.getAttributeValue(Constants.aciDefaultDeferralLimitByPercent)!= null ?
							aciCsf.getAttributeValue(Constants.aciDefaultDeferralLimitByPercent) : "";
				}
				 
				boolean hasDefaultLimitPercent = false;
				boolean hasDefaultLimitDollar = false;
				
				 if (!"".equals(defaultLimitByDollar))
					 hasDefaultLimitDollar = true;
				 if (!"".equals(defaultLimitByPercent))
					 hasDefaultLimitPercent = true;
								 
				 if (Constants.DEFERRAL_TYPE_DOLLAR.equals(deferralType)) {
					 
					 if (includeIncreaseRate){
						 aciCsfInfo.append(COMMA);
						 aciCsfInfo.append(DOUBLE_QUOTES); // default annual Increase Rate
						 aciCsfInfo.append("$" + DataUtility.formatCurrency(aciCsf.getAttributeValue(Constants.aciDefaultAnnualIncreaseByAmount)));
						 aciCsfInfo.append(DOUBLE_QUOTES);
					 }
					 if (includePlanDefaultMaxLimit){
						 aciCsfInfo.append(COMMA);
						 aciCsfInfo.append(DOUBLE_QUOTES); // plan default Limit
						 if (hasDefaultLimitDollar)
							 aciCsfInfo.append("$" + defaultLimitByDollar);
						 else 
							 aciCsfInfo.append("");
						 aciCsfInfo.append(DOUBLE_QUOTES);
					 }
					 
				 }else if (Constants.DEFERRAL_TYPE_PERCENT.equals(deferralType)) {
					 
					 if (includeIncreaseRate){
						 aciCsfInfo.append(COMMA);
						 aciCsfInfo.append(DOUBLE_QUOTES); // default annual Increase Rate
						 if(StringUtils.equals(Constants.ACI_AUTO, signUpMethod)) {
						     aciCsfInfo.append(planData.getAciDefaultIncreasePercent() == null ? "" : planData.getAciDefaultIncreasePercent().intValue() + "%");
						 } else {
							 aciCsfInfo.append(aciCsf.getAttributeValue(Constants.aciDefaultAnnualIncreaseByPercent) + "%");
						 }
						 aciCsfInfo.append(DOUBLE_QUOTES);
					 }
					 if (includePlanDefaultMaxLimit){
						 aciCsfInfo.append(COMMA);
						 aciCsfInfo.append(DOUBLE_QUOTES); // plan default Limit
						 if (hasDefaultLimitPercent)
							 aciCsfInfo.append(defaultLimitByPercent + "%");
						 else 
							 aciCsfInfo.append("");
						 aciCsfInfo.append(DOUBLE_QUOTES);
					 }
					 
				 }else {
					 
					 if (includeIncreaseRate){
						 aciCsfInfo.append(COMMA);
						 aciCsfInfo.append(DOUBLE_QUOTES); // default annual Increase Rate
						 String aid = aciCsf.getAttributeValue(Constants.aciDefaultAnnualIncreaseByAmount);
						 if(StringUtils.isEmpty(aid)) {
							 aciCsfInfo.append("$" + "");
						 } else {
							 aciCsfInfo.append("$" + DataUtility.formatCurrency(aid));
						 }
						 aciCsfInfo.append(" or ");
						 if(StringUtils.equals(Constants.ACI_AUTO, signUpMethod)) {
							 Integer value = planData.getAciDefaultIncreasePercent().intValue();
						     aciCsfInfo.append( value == null  ? "" : value + "%");
						 } else {
							 aciCsfInfo.append(aciCsf.getAttributeValue(Constants.aciDefaultAnnualIncreaseByPercent) + "%");
						 }
						 aciCsfInfo.append(DOUBLE_QUOTES);
					 }
					 if (includePlanDefaultMaxLimit){
						 aciCsfInfo.append(COMMA);
						 aciCsfInfo.append(DOUBLE_QUOTES); // plan default Limit
						 if (hasDefaultLimitDollar)
							 aciCsfInfo.append("$" + defaultLimitByDollar);
						 if (hasDefaultLimitDollar && hasDefaultLimitPercent)
							 aciCsfInfo.append(" or ");
						 if(hasDefaultLimitPercent)
							 aciCsfInfo.append(defaultLimitByPercent + "%");
						 if (!hasDefaultLimitDollar && !hasDefaultLimitPercent)
							 aciCsfInfo.append("");
						 aciCsfInfo.append(DOUBLE_QUOTES);
					 }
				 }
				 
				// request.setAttribute(ANNIVERSARY_DATE, aciCsf.getAttributeValue(Constants.aciDefaultAnniversaryDate));
			 }
			
			return aciCsfInfo;
	}
	
	/**
	 * Formats the participant Anniversary Date in the 'MMMM dd' format if available
	 * else formats the contract anniversary date
	 * @param parAnnDate, request
	 * @return
	 *//*
	public String getAnniversaryDate(Date parAnnDate,HttpServletRequest request){
		String formattedDate = "";
		if (parAnnDate != null){
			formattedDate = SHORT_DATE_FORMAT.format(parAnnDate);
		}else{
			Object attribute = request.getAttribute(ANNIVERSARY_DATE);
			String contractAnnivDate = attribute!=null? attribute.toString():"";
			if (!"".equals(contractAnnivDate)){
				try {
					Date annivDate = STANDARD_DATE_FORMAT.parse(contractAnnivDate);
					formattedDate = SHORT_DATE_FORMAT.format(annivDate);
				} catch (Exception e){
				// do nothing
				}
			}
		}
		return formattedDate;
	}*/
	
	/**
	 * Stream the download data to the Response. This method is singled out and
	 * made public static so that non-ReportActions can use it. This method
	 * closes the Response's OutputStream when it returns.
	 * 
	 * @param request
	 * 				HttpServletRequest
	 * @param response
	 * 				HttpServletResponse
	 * @param contentType
	 * 				String
	 * @param fileName
	 * 				String
	 * @param downloadData
	 * 				byte[]
	 * 
	 * @throws IOException
	 */
	public static void streamDownloadData(HttpServletRequest request,
			HttpServletResponse response, String contentType, String fileName,
			byte[] downloadData) throws IOException {
	
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); 
        response.setHeader("Pragma", "no-cache");
		response.setContentType(contentType);
		String userAgent = request.getHeader("User-Agent");
		if (userAgent == null || userAgent.indexOf("MSIE 5") < 0) {
			response.setHeader(CONTENT_DISPOSITION_TEXT, ATTACHMENT_TEXT
					+ fileName);
		}
		response.setContentLength(downloadData.length);
		try {
			response.getOutputStream().write(downloadData);
		} finally {
			response.getOutputStream().close();
		}
	}
	
	/**
	 * Generates the file name for the merge file based on 
	 * the template.
	 * 
	 * @param request
	 * 				HttpServletRequest
	 * @param isACIOnlyTemplate
	 * 				boolean
	 * @param isUpcominAnniversaryTemplate
	 * 				boolean
	 * 
	 * @return String
	 */
	private String getFileName(
			HttpServletRequest request, 
			boolean isACIOnlyTemplate,
			boolean isUpcominAnniversaryTemplate) {
		
		int contractNumber = getUserProfile(request).getCurrentContract().getContractNumber(); 
		String fileName = "";
		String dateString = null;
		synchronized (DATE_FORMATTER) {
			dateString = DATE_FORMATTER.format(new Date());
		}
		
		if (isACIOnlyTemplate) {
			fileName = contractNumber + "-" + dateString + CSV_EXTENSION;
		} else if (isUpcominAnniversaryTemplate){
			fileName = contractNumber + "-Participants_with_anniversaries_in_90_days-" + dateString + CSV_EXTENSION;
		} else {
			String nped = (String)request.getParameter("nped");
			String ped = nped.substring(5,7)+nped.substring(8)+nped.substring(0,4);
			fileName = contractNumber + " PED " + ped + " ON " + dateString + CSV_EXTENSION;
		}
		
		return fileName;
	}

	/**
	 * Returns the contract address. This method is called only when 
	 * the address of the employee or participant is not available
	 * 
	 * @param request 
	 * 				HttpServletRequest
	 * 
	 * @return	String
	 * 
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	private String getContractAddress(
			HttpServletRequest request) throws IOException, ServletException, SystemException{
		
		ContractProfileVO vo = null;
		Contract currentContract = getUserProfile(request).getCurrentContract();

		vo = ContractServiceDelegate.getInstance().getContractProfileDetails(
				currentContract.getContractNumber(), Environment.getInstance().getSiteLocation());

		StringBuffer contractAddressBuffer = new StringBuffer();
		
		contractAddressBuffer.append(DOUBLE_QUOTES);
		contractAddressBuffer.append(vo.getAddress().getLine1() == null ? "" : vo.getAddress().getLine1()); // address line1
		contractAddressBuffer.append(DOUBLE_QUOTES);
		contractAddressBuffer.append(COMMA);
		contractAddressBuffer.append(DOUBLE_QUOTES); //  address 2
		contractAddressBuffer.append(vo.getAddress().getline2() == null ? "" : vo.getAddress().getline2());
		contractAddressBuffer.append(DOUBLE_QUOTES);
		contractAddressBuffer.append(COMMA);
		contractAddressBuffer.append(DOUBLE_QUOTES); //  city
		contractAddressBuffer.append(vo.getAddress().getCity() == null ? "" : vo.getAddress().getCity());
		contractAddressBuffer.append(DOUBLE_QUOTES);
		contractAddressBuffer.append(COMMA);
		contractAddressBuffer.append(DOUBLE_QUOTES); //  state
		contractAddressBuffer.append(vo.getAddress().getStateCode() == null ? "" : vo.getAddress().getStateCode());
		contractAddressBuffer.append(DOUBLE_QUOTES);
		contractAddressBuffer.append(COMMA);
		contractAddressBuffer.append(DOUBLE_QUOTES); //  country
		contractAddressBuffer.append("");
		contractAddressBuffer.append(DOUBLE_QUOTES);
		contractAddressBuffer.append(COMMA);
		contractAddressBuffer.append(DOUBLE_QUOTES); //  zip
		contractAddressBuffer.append(vo.getAddress().getZipCode() == null ? "" : vo.getAddress().getZipCode());
		contractAddressBuffer.append(DOUBLE_QUOTES);
		contractAddressBuffer.append(COMMA);
		
		return contractAddressBuffer.toString();
	}
	
	/**
	 * Returns TRUE, if address is available for the participant
	 * 
	 * @param pVo
	 * 			ParticipantVO
	 * 
	 * @return boolean
	 */
	public boolean checkParticipantAddressAvailable(ParticipantVO pVo){
		if (pVo.getAddressLine1() == null && 
				pVo.getAddressLine2() == null &&
				pVo.getCityName() == null && 
				pVo.getZipCode() == null)
			return false;
		return true;
	}

	/**
	 * Returns TRUE, if address is available for the employee
	 * 
	 * @param e
	 * 			EmployeeVO
	 * 
	 * @return boolean
	 */
	public boolean checkEmployeeAddressAvailable(EmployeeVO e){
		if (e.getAddressLine1() == null && 
				e.getAddressLine2() == null &&
				e.getCityName() == null && 
				e.getZipCode() == null)
			return false;
		return true;
	}
	
	/**
	 * Finds the increase rate based on the personalized and default values 
	 * 
	 * @param pVo 
	 * 			ParticipantVO
	 * @param contractNumber
	 * 			int
	 * 
	 * @return String
	 * 
	 * @throws ApplicationException
	 * @throws SystemException
	 */
	private String determineIncreaseAmountOrRate(
			ParticipantVO pVo, 
			int contractNumber) throws SystemException {
		String aciIncrease = null;;
		String defaultIncreaseByDollar = null;
		String defaultIncreaseByPercent = null;

		String personalizedIncreasePct = pVo.getDeferIncPct();
		String personalizedIncreaseAmt = pVo.getDeferIncAmt();
		
		
		if (personalizedIncreasePct != null) {
			aciIncrease = DataUtility.formatCurrencyWithoutDecimals(personalizedIncreasePct) + Constants.DEFERRAL_TYPE_PERCENT;
		} else if (personalizedIncreaseAmt != null){ 
			aciIncrease = Constants.DEFERRAL_TYPE_DOLLAR + DataUtility.formatCurrency(personalizedIncreaseAmt);
		} else {
			try {
				ContractServiceFeature aciCsf = 
					ContractServiceDelegate.getInstance().getContractServiceFeature(
							contractNumber,ServiceFeatureConstants.AUTO_CONTRIBUTION_INCREASE);
				
				PlanDataLite planData = ContractServiceDelegate.getInstance().getPlanDataLight(contractNumber);
				String signUpmethod = ContractServiceDelegate.getInstance().determineSignUpMethod(contractNumber);
				
				if (StringUtils.equals(Constants.ACI_AUTO, signUpmethod)
						|| StringUtils.equals(Constants.ACI_SIGNUP, signUpmethod)) {
					
					ContractServiceFeature mdCsf = 
					 ContractServiceDelegate.getInstance().getContractServiceFeature(
							 contractNumber, Constants.MANAGING_DEFERRALS);
				 	
					String deferralType = mdCsf.getAttributeValue(Constants.medDeferralType);
					
					if (Constants.DEFERRAL_TYPE_DOLLAR.equals(deferralType)) {
						defaultIncreaseByDollar = Constants.DEFERRAL_TYPE_DOLLAR + DataUtility.formatCurrency(aciCsf.getAttributeValue(Constants.aciDefaultAnnualIncreaseByAmount));
						aciIncrease = defaultIncreaseByDollar;
					} else if (Constants.DEFERRAL_TYPE_PERCENT.equals(deferralType)) {
						if(StringUtils.equals(Constants.ACI_AUTO, signUpmethod)) {
							defaultIncreaseByPercent = planData.getAciDefaultIncreasePercent().intValue() + Constants.DEFERRAL_TYPE_PERCENT;
							aciIncrease = defaultIncreaseByPercent;
						} else {
							defaultIncreaseByPercent = aciCsf.getAttributeValue(Constants.aciDefaultAnnualIncreaseByPercent) + Constants.DEFERRAL_TYPE_PERCENT;
							aciIncrease = defaultIncreaseByPercent;
						}
					} else if (Constants.DEFERRAL_TYPE_EITHER.equals(deferralType)) {
						
						defaultIncreaseByDollar = Constants.DEFERRAL_TYPE_DOLLAR + DataUtility.formatCurrency(aciCsf.getAttributeValue(Constants.aciDefaultAnnualIncreaseByAmount));
						defaultIncreaseByPercent = aciCsf.getAttributeValue(Constants.aciDefaultAnnualIncreaseByPercent) + Constants.DEFERRAL_TYPE_PERCENT;
						
						if (getIncreaseRateFromPersonalLimit(pVo, defaultIncreaseByDollar, defaultIncreaseByPercent) != null){
							aciIncrease = getIncreaseRateFromPersonalLimit(pVo, defaultIncreaseByDollar, defaultIncreaseByPercent);
						} else if (getIncreaseRateFromOutstandingAdhocRequest(pVo, defaultIncreaseByDollar, defaultIncreaseByPercent) != null) {
							aciIncrease = getIncreaseRateFromOutstandingAdhocRequest(pVo, defaultIncreaseByDollar, defaultIncreaseByPercent);
						} else if (getIncreaseRateFromFromConfirmedValues(pVo, deferralType, defaultIncreaseByDollar, defaultIncreaseByPercent) != null) {
							aciIncrease = getIncreaseRateFromFromConfirmedValues(pVo, deferralType, defaultIncreaseByDollar, defaultIncreaseByPercent);
						} else {
							aciIncrease = defaultIncreaseByPercent;
						}
					}
				}
			} catch (ApplicationException ae){
				throw new SystemException(ae.getMessage());
			}
		}
		return aciIncrease;
	}
	
	/**
	 * Method generates the Employee merge file in .csv format
	 * for the EC word template and puts in the output stream
	 * 
	 * @param request
	 * 				HttpServletRequest
	 * @param response
	 * 				HttpServletResponse
	 * @throws SystemException
	 * @throws ServletException
	 * @throws IOException
	 */
	public void getEmployeeMergeFileForEC(
			HttpServletRequest request, 
			HttpServletResponse response) throws SystemException,ServletException, IOException{
		
		Contract currentContract = getUserProfile(request).getCurrentContract();
		
		try{
			ContractServiceFeature csf = com.manulife.pension.delegate.ContractServiceDelegate.getInstance().getContractServiceFeature(currentContract.getContractNumber(), ServiceFeatureConstants.ELIGIBILITY_CALCULATION_CSF);
			StringBuffer buffer = new StringBuffer(COLUMN_LABELS_EC);
			buffer.append(LINE_BREAK);
			
			String ecValue = csf.getValue();
			if(ContractServiceFeature.internalToBoolean(ecValue).booleanValue())
			{
				String nped = (String)request.getParameter("nped");
				Date applicableNextPlanEntryDate = null;
				String ped = null;
				if(nped != null)
				{
					try{
						applicableNextPlanEntryDate = DATE_FORMATTER1.parse(nped);
						applicableNextPlanEntryDate = STANDARD_DATE_FORMAT.parse(STANDARD_DATE_FORMAT.format(applicableNextPlanEntryDate));
						ped = DATE_FORMATTER3.format(applicableNextPlanEntryDate);
					}catch(ParseException pe)
					{
						// do nothing  - default to next plan entry date
					}
				}
				
				
				
				List<EmployeeVO> eligibleEmployees = ContractServiceDelegate.getInstance()
						.getEligibleEmployees(currentContract.getContractNumber(), applicableNextPlanEntryDate);
				
				if(eligibleEmployees != null)
				{
					Iterator<EmployeeVO> eeIt = eligibleEmployees.iterator();
										
					while(eeIt.hasNext())
					{
					    EmployeeVO e = (EmployeeVO)eeIt.next();
						buffer.append(DOUBLE_QUOTES); // 1 first name 
						buffer.append(e.getFirstName() == null ? "" : e.getFirstName());
						buffer.append(DOUBLE_QUOTES);
						buffer.append(COMMA);
						buffer.append(DOUBLE_QUOTES); // 2 initial
						if(e.getMiddleInitial() != null && e.getMiddleInitial().trim().length() > 0 ){
							buffer.append(e.getMiddleInitial()); 
						}
						else{ 
							buffer.append("");
						}
						buffer.append(DOUBLE_QUOTES);
						buffer.append(COMMA);
						buffer.append(DOUBLE_QUOTES); // 2 initial
						buffer.append(e.getLastName() == null ? "" : e.getLastName()); //3 last name 
						buffer.append(DOUBLE_QUOTES);
						buffer.append(COMMA);
						if (checkEmployeeAddressAvailable(e)){
							
							buffer.append(DOUBLE_QUOTES); //  address 1 
							buffer.append(e.getAddressLine1() != null ? e.getAddressLine1() :""); 
							buffer.append(DOUBLE_QUOTES);
							buffer.append(COMMA);
							buffer.append(DOUBLE_QUOTES); //  address 2
							buffer.append(e.getAddressLine2() != null ? e.getAddressLine2() :""); 
							buffer.append(DOUBLE_QUOTES);
							buffer.append(COMMA);
							buffer.append(DOUBLE_QUOTES); //  city
							buffer.append(e.getCityName()!= null ? e.getCityName() :""); 
							buffer.append(DOUBLE_QUOTES);
							buffer.append(COMMA);
							buffer.append(DOUBLE_QUOTES); //  state
							buffer.append(e.getStateCode() != null ? e.getStateCode() :"");
							buffer.append(DOUBLE_QUOTES);
							buffer.append(COMMA);
							buffer.append(DOUBLE_QUOTES); //  country
							buffer.append(e.getCountryName() != null ? e.getCountryName() :"");
							buffer.append(DOUBLE_QUOTES);
							buffer.append(COMMA);
							buffer.append(DOUBLE_QUOTES); //  zip
							buffer.append(e.getZipCode() != null ? e.getZipCode() :"");
							buffer.append(DOUBLE_QUOTES);
							buffer.append(COMMA);
							
							
						}else {
							buffer.append(getContractAddress(request));
						}
						buffer.append(DOUBLE_QUOTES);
						buffer.append(currentContract.getCompanyName() == null ? "" :currentContract.getCompanyName().trim()); // short contract name
						buffer.append(DOUBLE_QUOTES);
						buffer.append(COMMA);
						buffer.append(DOUBLE_QUOTES); // 11 contract number
						buffer.append(currentContract.getContractNumber());
						buffer.append(DOUBLE_QUOTES);
						buffer.append(COMMA);
						buffer.append(DOUBLE_QUOTES);
						buffer.append(ped);  // 12 Plan Entry Date
						buffer.append(DOUBLE_QUOTES);
						buffer.append(LINE_BREAK);
					}
				}
						
				String fileName = getFileName(request, false, false);
				fileName = fileName.replaceAll("\\ ", "_");
				
				streamDownloadData(request, response, CSV_TEXT,
					fileName, buffer.toString().getBytes());
			}
		}catch(ApplicationException ae) {
			throw new SystemException(ae.getMessage());
		}
	}
	
	/**
	 * Checks the participamt's personalized limit value and returns the
	 * deferral type matching to the personalized value
	 *  
	 * @param pVo ParticipantVO
	 * @param defaultIncreaseByDollar String
	 * @param defaultIncreaseByPercent String
	 * 
	 * @return String 
	 */
	private String getIncreaseRateFromPersonalLimit(ParticipantVO pVo,String defaultIncreaseByDollar,String defaultIncreaseByPercent) {
		BigDecimal personalLimitByDollar = pVo.getDeferMaxLimitAmt();
		BigDecimal personalLimitByPercent = pVo.getDeferMaxLimitPct();
		String increaseRate = null;
		
		if (personalLimitByDollar != null){
			increaseRate = defaultIncreaseByDollar;
		} else if (personalLimitByPercent != null) {
			increaseRate = defaultIncreaseByPercent;
		}
		return increaseRate;
	}

	/**
	 * Checks for the most outstanding adhoc request and returns the 
	 * deferral type matching to the requested value
	 *  
	 * @param pVo ParticipantVO
	 * @param defaultIncreaseByDollar String
	 * @param defaultIncreaseByPercent String
	 * 
	 * @return String 
	 */
	private String getIncreaseRateFromOutstandingAdhocRequest(ParticipantVO pVo,String defaultIncreaseByDollar,String defaultIncreaseByPercent) {
		String increaseRate = null; 
		
		// If the processed code is AP and the contrb instrn src code is not for ACI,
		// then return the deferral type of the requested value
		if (OUTSTANDING_PROCESS_CODE.equals(pVo.getProcessedStatusCode()) &&
				!ACI_CONTRIBUTION_INSTRN_SRC_CODE.equals(pVo.getContributionInstructSrcCode())) {
			if (pVo.getContributionAmt() != null) {
				increaseRate = defaultIncreaseByDollar;
			} else {
				increaseRate = defaultIncreaseByPercent;
			}
		}
		return increaseRate;
	}

	/**
	 * Checks for the confirmed before tax and roth value returns the 
	 * deferral type matching to the confirmed value
	 *  
	 * @param pVo ParticipantVO
	 * @param deferralType String
	 * @param defaultIncreaseByDollar String
	 * @param defaultIncreaseByPercent String
	 * 
	 * @return String 
	 */
	private String getIncreaseRateFromFromConfirmedValues(ParticipantVO pVo, String deferralType, String defaultIncreaseByDollar,String defaultIncreaseByPercent) {
		String increaseRate = findIncreaseRate(
				pVo.getBeforeTaxDeferAmt(), pVo.getBeforeTaxDeferPct(), deferralType, defaultIncreaseByDollar, defaultIncreaseByPercent);
		
		if ( increaseRate == null){
			increaseRate = findIncreaseRate(
				pVo.getDesigRothDefAmt(), pVo.getDesigRothDefPct(), deferralType, defaultIncreaseByDollar, defaultIncreaseByPercent);
		}
		return increaseRate;
	}
	
	/**
	 * Returns the default increase rate based on the confirmed before tax
	 * and roth value
	 *  
	 * @param dollarValue BigDecimal
	 * @param percentValue BigDecimal
	 * @param deferralType String
	 * @param defaultIncreaseByDollar String
	 * @param defaultIncreaseByPercent String
	 * 
	 * @return String 
	 */
	private String findIncreaseRate(
			BigDecimal dollarValue, BigDecimal percentValue, String deferralType, String defaultIncreaseByDollar,String defaultIncreaseByPercent) {
		String increaseRate = null;

		// dollar = percent = null;return blank
		if (dollarValue == null && percentValue == null){ 
			increaseRate = null;
		//dollar = null, percent = value; return percent
		}else if (dollarValue == null && percentValue != null){ 
			increaseRate = defaultIncreaseByPercent;
		// dollar = value, percent = null, return dollar
		}else if (dollarValue != null && percentValue == null){ 
			increaseRate = defaultIncreaseByDollar;
		// dollar = percent = value			
		}else if (dollarValue != null && percentValue != null){ 
			// dollar = percent = 0
			if (dollarValue.compareTo(ZERO_DOLLAR) == 0 && percentValue.compareTo(ZERO) == 0){
				// if csf deferral type is % return %;
				if (Constants.DEFERRAL_TYPE_PERCENT.equals(deferralType)) {
					increaseRate = defaultIncreaseByPercent;
				// if csf deferral type is $ return $;					
				}else if (Constants.DEFERRAL_TYPE_DOLLAR.equals(deferralType)) {
					increaseRate = defaultIncreaseByDollar;
				}
			// if one of the dollar or percent value is greater than zero,
			// compare the values and return deferral type of greater value.
			}else if (dollarValue.compareTo(percentValue) <= 0) {
				increaseRate = defaultIncreaseByPercent;
			}else {
				increaseRate = defaultIncreaseByDollar;
			}
		}
		return increaseRate;
	}
	
	private String getPlanFrequency(int contractId) throws SystemException{
	    
	    return com.manulife.pension.delegate.ContractServiceDelegate.getInstance().getPlanFrequency(contractId);
	    
	}
	
	/*
	 * gets the ppt anniversary date
	 */
	private String getParticipantNextAnniversayDate(int contractId,
			String profileId, Date pptnextAnniversaryDate,
			PlanDataLite planData, boolean isAuto,
			List<ParticipantVO> mostRecentEzIncreaseContributionChangeRequest)
			throws SystemException {

		Calendar dateOfIncrease = Calendar.getInstance();
		if (pptnextAnniversaryDate == null) {
			dateOfIncrease = getContractNextAnniversayDate(planData, isAuto);
		} else {
			ParticipantVO recentEzIncreaseContributionChangeRequest = getMostRecentEzIncreaseContributionChangeRequest(
					profileId, mostRecentEzIncreaseContributionChangeRequest);

			if (recentEzIncreaseContributionChangeRequest != null
					&& StringUtils.equals("PA",recentEzIncreaseContributionChangeRequest.getProcessedStatusCode())) {
				Date nextAnniversay = getNextAnniversary(recentEzIncreaseContributionChangeRequest.getAciAnniversaryDate());
				if(nextAnniversay != null){
					dateOfIncrease.setTime(nextAnniversay);
				}
			} else {
				dateOfIncrease.setTime(pptnextAnniversaryDate);
			}
		}
		return SHORT_DATE_FORMAT.format(dateOfIncrease.getTime());
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
	 * Returns the current date
	 * 
	 * @return Date
	 */
	private Date getCurrentDate() {
		GregorianCalendar cal = new GregorianCalendar();
		cal.set(Calendar.HOUR,0);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.MILLISECOND,0);
		cal.set(Calendar.AM_PM, Calendar.AM);
		return cal.getTime();
	}
	
	/**
	 * This methods returns the most recent ez increase change contribution
	 * request
	 * 
	 * @param contractId
	 * @param profileId
	 * @return ParticipantVO
	 */
	private ParticipantVO getMostRecentEzIncreaseContributionChangeRequest(String profileId, 
			List<ParticipantVO> mostRecentEzIncreaseContributionChangeRequest) throws SystemException {
		for (ParticipantVO vo : mostRecentEzIncreaseContributionChangeRequest) {
			if(profileId.equals(vo.getProfileId()) ) {
				return vo;
			}
		}
		return null;
	}
	
	/**
	 * Calculates  Next Anniversary date according to the specified rule
	 * and returns it
	 * 
	 * @param aciAnnivDate
	 * @return Date
	 */
	private  Date getNextAnniversary(Date aciAnnivDate) {
		Date nextAnnivDate = null;
		if (aciAnnivDate != null) {
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(aciAnnivDate);
			if (getCurrentDate().after(gc.getTime())) {
				gc.add(Calendar.YEAR, 1);
			}
			// PPC.20.11
			if (gc.get(Calendar.MONTH) == 1 && gc.get(Calendar.DAY_OF_MONTH) == 29) {
				gc.add(Calendar.DAY_OF_YEAR, 1);
			}
			nextAnnivDate = gc.getTime();	
		}
		return nextAnnivDate;
	}
	
	
	//CL119569 fix - Mail Merge File scheduledDeferral EZI incorrect - start
    private static Date getNextDateFrom(Date startDate, int amount, int offset)
    {
        Calendar calendar = GregorianCalendar.getInstance();
        if(startDate != null)
        {
            calendar.setTime(startDate);
        }
        
        if (offset == OFFSET_BY_MONTH) {
            calendar.add(Calendar.DAY_OF_MONTH, amount);
        } else if (offset == OFFSET_BY_YEAR){
            calendar.add(Calendar.YEAR, amount);
        }
        return calendar.getTime();
    }    

	/**
	 * Method to compare the dates with input VO date, current date and next 90 days date
	 * 
	 * With respect to current date, participants whose anniversary falls within next 90 days, 
	 * inclusive of today and the 90th day, should be included in the SDI report. 
	 * 
	 * @param Date
	 * @return boolean
	 */
	private boolean isAnniversaryWithinNext90Days(Date aciAnniversaryDate) throws ParseException{
		// excludes current date but includes exactly next 90th day
		
		if(!(aciAnniversaryDate.toString()).trim().equals(""))
		{
			Date today = getCurrentDate();
			Date ninetyDaysFromToday = getNextDateFrom(today, 90, OFFSET_BY_MONTH);

			if(aciAnniversaryDate.compareTo(today) == 0){
			    return true;
			} else if(aciAnniversaryDate.after(today) && (aciAnniversaryDate.compareTo(ninetyDaysFromToday) <= 0)){
	            return true;
	        } else if (aciAnniversaryDate.before(today)){
	        	
	        	Date rolledUpAnniversaryDate = rollupAnniversaryDate(aciAnniversaryDate, today);
	        	
	        	if(rolledUpAnniversaryDate.compareTo(today) == 0){
	        		return true;
	        	} else if(rolledUpAnniversaryDate.after(today) && (rolledUpAnniversaryDate.compareTo(ninetyDaysFromToday) <= 0)){
		            return true;
		        } else {
	                return false;
	            }
	        }
		
		}
		return false;
	}
	
	/** 
	 * ACI_ANNIVERSARY_DATE in EMPLOYEE_CONTRACT table doesn't roll with every year. 
	 * If a participant had an anniversary date set to 14-FEB-2009, it won't be updated to 14-FEB-2012 for 2012.   
	 * However, the anniversary will still be on 14-FEB each year. So for ACI or SDI comparisons, we need to 
	 * roll this date w.r.t to current date, and then apply the filtering logic for the SDI report.    
	 */
    private static Date rollupAnniversaryDate(Date aciAnniversaryDate, Date today) {
    	Date rolledUpAnniversaryDate = null;
    	
    	GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(aciAnniversaryDate);
		
		do {
			calendar.add(Calendar.YEAR, 1);
			rolledUpAnniversaryDate = calendar.getTime();
			
		} while (rolledUpAnniversaryDate.before(today));
		
		return rolledUpAnniversaryDate;
	}	
	//CL119569 fix - Mail Merge File scheduledDeferral EZI incorrect - end
    
    /**
	 * * (non-Javadoc) This code has been changed and added to Validate form and
	 * request against penetration attack, prior to other validations.
	 */
    @Autowired
	   private PSValidatorFWInput  psValidatorFWInput;
    @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind(request);
	    binder.addValidators(psValidatorFWInput);
	}
    
}
