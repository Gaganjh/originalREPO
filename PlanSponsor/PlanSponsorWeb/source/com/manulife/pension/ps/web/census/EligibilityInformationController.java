package com.manulife.pension.ps.web.census;

import static com.manulife.pension.platform.web.CommonConstants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
import static com.manulife.pension.platform.web.CommonConstants.PS_APPLICATION_ID;
import static com.manulife.pension.ps.web.Constants.EXTERNAL_USER_ID_TYPE;
import static com.manulife.pension.ps.web.Constants.INTERNAL_USER_ID_TYPE;
import static com.manulife.pension.service.contract.util.ServiceFeatureConstants.ELIGIBILITY_CALCULATION_CSF;
import static com.manulife.pension.service.contract.util.ServiceFeatureConstants.YES;
import static com.manulife.pension.service.eligibility.EligibilityRequestConstants.ELIGIBILITY_CALCULATION;
import static com.manulife.pension.service.eligibility.EligibilityRequestConstants.SERVICE_CREDIT_METHOD_HOS;
import static com.manulife.util.render.RenderConstants.MEDIUM_MDY_SLASHED;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;
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

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.EligibilityServiceDelegate;
import com.manulife.pension.delegate.EmployeeServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.PsAutoController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWDefault;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.contract.valueobject.DayOfYear;
import com.manulife.pension.service.contract.valueobject.MoneyTypeVO;
import com.manulife.pension.service.contract.valueobject.PlanData;
import com.manulife.pension.service.eligibility.EligibilityRequestConstants;
import com.manulife.pension.service.eligibility.EligibilityRequestConstants.RequestCauseTypeCode;
import com.manulife.pension.service.eligibility.EligibilityRequestConstants.SourceChannelCode;
import com.manulife.pension.service.eligibility.EligibilityRequestConstants.SourceFunctionCode;
import com.manulife.pension.service.eligibility.util.LongTermPartTimeAssessmentUtil;
import com.manulife.pension.service.eligibility.valueobject.EligibilityRequestVO;
import com.manulife.pension.service.eligibility.valueobject.EmployeePlanEntryVO;
import com.manulife.pension.service.eligibility.valueobject.PlanEntryRequirementDetailsVO;
import com.manulife.pension.service.employee.eligibility.constants.EmployeeEligibilityAssessmentReqConstants;
import com.manulife.pension.service.employee.valueobject.Employee;
import com.manulife.pension.service.environment.valueobject.LabelValueBean;
import com.manulife.pension.util.log.ServiceLogRecord;
import com.manulife.util.render.DateRender;

/*
 * Action class for eligibility information page
 * @author Siby Thomas
 * 
 * Modified By
 * @author Murali Chandran
 * 		Modified logic for calculation of plan entry dates in getPlanEntryDates method
 *
 */

@Controller
@RequestMapping(value = "/census")
@SessionAttributes({ "eligibilityInformationForm" })

public class EligibilityInformationController extends PsAutoController {

	@ModelAttribute("eligibilityInformationForm")
	public EligibilityInformationForm populateForm() {
		return new EligibilityInformationForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("default", "/census/eligibilityInformation.jsp");
		forwards.put("print", "/census/eligibilityInformation.jsp");
		forwards.put("calculateEligibility", "redirect:/do/census/eligibilityInformation/?default");
	}

	private Category interactionLog = Category.getInstance(ServiceLogRecord.class);

	private ServiceLogRecord logRecord = new ServiceLogRecord("EligibilityInformationAction");

	private static final String DEFAULT_FORWARD = "default";

	private static final String CALCULATE_ELIGIBILITY_FORWARD = "calculateEligibility";

	private static enum PENDING_REQUEST_TYPES {
		EMPLOYEE_LEVEL_ELIGIBILITY_CALACULATION, PLAN_LEVEL_ELIGIBILITY_CALACULATION, EMPLOYEE_LEVEL_PED_CALACULATION,
		PLAN_LEVEL_PED_CALACULATION
	};

	public EligibilityInformationController() {
		super(EligibilityInformationController.class);
	}

	/**
	 * The preExecute method has been overridden so that the page can be prevented
	 * from being accessed through book marking if the EC feature is OFF
	 * 
	 */
	protected String preExecute(ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SystemException {

		super.preExecute(form, request, response);

		// Get the Principal from the request
		UserProfile userProfile = getUserProfile(request);

		// get the user profile object and set the current contract to null
		Contract currentContract = userProfile.getCurrentContract();

		ContractServiceDelegate delegate = ContractServiceDelegate.getInstance();
		ContractServiceFeature eligibilityCalculationCSF = null;
		try {
			eligibilityCalculationCSF = delegate.getContractServiceFeature(currentContract.getContractNumber(),
					ELIGIBILITY_CALCULATION_CSF);
		} catch (ApplicationException exception) {
			throw new SystemException(exception,
					"Error thrown while calling EC csf for contractNum " + currentContract.getContractNumber());
		}

		// checking if EC service feature is off
		// hence redirect to home page.
		if (eligibilityCalculationCSF == null || !YES.equals(eligibilityCalculationCSF.getValue())) {
			return forwards.get(HOMEPAGE_FINDER_FORWARD_REDIRECT);
		}

		return null;
	}

	/**
	 * Invoked for Default action. Populates the money types and employee
	 * information
	 * 
	 * @see PsAutoController#doDefault(ActionMapping, PsAutoForm,
	 *      HttpServletRequest, HttpServletResponse)
	 */
	@RequestMapping(value = "/eligibilityInformation/", method = { RequestMethod.GET })
	public String doDefault(@Valid @ModelAttribute("eligibilityInformationForm") EligibilityInformationForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(actionForm, request, response);
		if (StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("defualt");// if input
																											// forward
																											// not
																											// //available,
																											// provided
																											// default
			}
		}

		if (logger.isDebugEnabled())
			logger.debug(
					EligibilityInformationController.class.getName() + ":forwarding to Eligibility Information Page.");

		EmployeeServiceDelegate employeeServiceDelegate = EmployeeServiceDelegate.getInstance(PS_APPLICATION_ID);

		EligibilityServiceDelegate eligibilityServiceDelegate = EligibilityServiceDelegate
				.getInstance(PS_APPLICATION_ID);

		EligibilityInformationForm theForm = (EligibilityInformationForm) actionForm;

		// Get the Principal from the request
		UserProfile userProfile = getUserProfile(request);

		// get the user profile object and set the current contract to null
		Contract currentContract = userProfile.getCurrentContract();

		// get employee information
		Employee employeeInfo = employeeServiceDelegate.getEmployeeByProfileId(new Long(theForm.getProfileId()),
				currentContract.getContractNumber(), null);
		theForm.setEmployeeDetails(employeeInfo);

		// calculate age of employee from DOB
		calculateEmployeeAge(theForm, employeeInfo.getEmployeeDetailVO().getBirthDate());

		// get the last invoked time stamp
		Timestamp eligibilityLastInvokedTime = employeeInfo.getEmployeeDetailVO().getEligibilityLastInvokedDateTime();

		// checking if there is any pending request or not
		Collection<EligibilityRequestVO> pendingRequestVoCollection = new ArrayList<EligibilityRequestVO>();
		Collection<EligibilityRequestVO> eligibilityRequestVoCollection = eligibilityServiceDelegate
				.getPendingPlanLevelOrEmployeeLevelRequests(currentContract.getContractNumber(),
						theForm.getProfileId());

		for (EligibilityRequestVO eligibilityRequestVO : eligibilityRequestVoCollection) {
			Timestamp eligibiltyLastUpdatedDateTime = eligibilityRequestVO.getLastUpdatedTime();
			if (eligibilityLastInvokedTime == null
					|| eligibiltyLastUpdatedDateTime.compareTo(eligibilityLastInvokedTime) >= 0) {
				pendingRequestVoCollection.add(eligibilityRequestVO);
			}
		}
		if (!pendingRequestVoCollection.isEmpty()) {
			theForm.setPendingEligibilityCalculationRequest(true);
		} else {
			theForm.setPendingEligibilityCalculationRequest(false);
		}
		theForm.setPendingRequestList(pendingRequestVoCollection);

		// sets the money type details list
		setEligibleMoneyTypeDetailsForContract(employeeServiceDelegate, eligibilityServiceDelegate,
				currentContract.getContractNumber(), theForm,currentContract);

		// get the plan entry dates
		List<Date> planEntryDates = getPlanEntryDates(currentContract.getContractNumber(), theForm);

		// calculated YTD data
		calculatePlanYTDData(employeeServiceDelegate, currentContract.getContractNumber(), theForm, planEntryDates);

		return forwards.get(DEFAULT_FORWARD);
	}

	/**
	 * Invoked when the calculate button is pressed on the page Calls eligibility
	 * interface and then refreshes the page
	 * 
	 * @param mapping    The action mapping.
	 * @param actionForm The action form.
	 * @param request    The HTTP request.
	 * @param response   The HTTP response.
	 * @return ActionForward The forward to process.
	 * @throws IOException      When an IO problem occurs.
	 * @throws ServletException When an Servlet problem occurs.
	 * @throws SystemException  When an generic application problem occurs.
	 */
	@RequestMapping(value = "/eligibilityInformation/", params = { "action=calculateEligibility" }, method = {
			RequestMethod.GET })
	public String doCalculateEligibility(
			@Valid @ModelAttribute("eligibilityInformationForm") EligibilityInformationForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if (StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("defualt");// if input
																											// forward
																											// not
																											// //available,
																											// provided
																											// default
			}
		}
// Get the Principal from the request
		UserProfile userProfile = getUserProfile(request);

		// get the user profile object and set the current contract to null
		Contract currentContract = userProfile.getCurrentContract();

		EligibilityServiceDelegate eligibilityServiceDelegate = EligibilityServiceDelegate
				.getInstance(PS_APPLICATION_ID);

		// if no pending request then
		// call the eligibility interface with default parameters
		if (!form.getPendingEligibilityCalculationRequest()) {

			calculateEligibility(eligibilityServiceDelegate,
					PENDING_REQUEST_TYPES.EMPLOYEE_LEVEL_ELIGIBILITY_CALACULATION, currentContract.getContractNumber(),
					form.getProfileId(), RequestCauseTypeCode.EMPLOYEE_DATA_UPDATE.getCode(),
					SourceChannelCode.PLAN_SPONSOR.getCode(), SourceFunctionCode.EEI.toString(), INTERNAL_USER_ID_TYPE,
					userProfile);

			return forwards.get(CALCULATE_ELIGIBILITY_FORWARD);
		}

		// get the pending request list
		Collection<EligibilityRequestVO> pendingRequests = form.getPendingRequestList();

		// pending request types
		List<EligibilityRequestVO> employeeLevelEligibilityRequests = new ArrayList<EligibilityRequestVO>();
		List<EligibilityRequestVO> planLevelEligibilityRequests = new ArrayList<EligibilityRequestVO>();
		List<EligibilityRequestVO> employeeLevelPEDRequests = new ArrayList<EligibilityRequestVO>();
		List<EligibilityRequestVO> planLevelPEDRequests = new ArrayList<EligibilityRequestVO>();

		for (EligibilityRequestVO eligibilityRequestVO : pendingRequests) {
			if (ELIGIBILITY_CALCULATION.equals(eligibilityRequestVO.getRequestTypeCode())) {
				if (eligibilityRequestVO.getProfileId() == form.getProfileId()) {
					employeeLevelEligibilityRequests.add(eligibilityRequestVO);
				} else {
					planLevelEligibilityRequests.add(eligibilityRequestVO);
				}
			} else {
				if (eligibilityRequestVO.getProfileId() == form.getProfileId()) {
					employeeLevelPEDRequests.add(eligibilityRequestVO);
				} else {
					planLevelPEDRequests.add(eligibilityRequestVO);
				}
			}
		}

		// find the latest updated the pending request and its type
		PENDING_REQUEST_TYPES pendingRequestType = null;
		EligibilityRequestVO latestRequestPending = null;
		if (!employeeLevelEligibilityRequests.isEmpty()) {
			latestRequestPending = getLatestPendingRequest(employeeLevelEligibilityRequests);
			pendingRequestType = PENDING_REQUEST_TYPES.EMPLOYEE_LEVEL_ELIGIBILITY_CALACULATION;
		} else if (!planLevelEligibilityRequests.isEmpty()) {
			latestRequestPending = getLatestPendingRequest(planLevelEligibilityRequests);
			pendingRequestType = PENDING_REQUEST_TYPES.PLAN_LEVEL_ELIGIBILITY_CALACULATION;
		} else if (!employeeLevelPEDRequests.isEmpty()) {
			latestRequestPending = getLatestPendingRequest(employeeLevelPEDRequests);
			pendingRequestType = PENDING_REQUEST_TYPES.EMPLOYEE_LEVEL_PED_CALACULATION;
		} else {
			latestRequestPending = getLatestPendingRequest(planLevelPEDRequests);
			pendingRequestType = PENDING_REQUEST_TYPES.PLAN_LEVEL_PED_CALACULATION;
		}

		// get user ID type
		String userIdType = "";
		if (userProfile.isInternalUser()) {
			userIdType = INTERNAL_USER_ID_TYPE;
		} else {
			userIdType = EXTERNAL_USER_ID_TYPE;
		}

		// calculate the eligibility/PED based on the type of pending request
		calculateEligibility(eligibilityServiceDelegate, pendingRequestType, currentContract.getContractNumber(),
				form.getProfileId(), RequestCauseTypeCode.EMPLOYEE_DATA_UPDATE.getCode(),
				latestRequestPending.getSourceChannelCode(), latestRequestPending.getSourceFunctionCode(), userIdType,
				userProfile);

		return forwards.get(CALCULATE_ELIGIBILITY_FORWARD);

	}

	/*
	 * The method set eligible money type details for the contract
	 * 
	 * @param employeeServiceDelegate
	 * 
	 * @param eligibilityServiceDelegate
	 * 
	 * @param contractId
	 * 
	 * @param profileId
	 * 
	 * @param theForm
	 * 
	 * @return Collection<EligibilityCalculationMoneyType>
	 * 
	 * @throws SystemException
	 */
	@SuppressWarnings("unchecked")
	private void setEligibleMoneyTypeDetailsForContract(EmployeeServiceDelegate employeeServiceDelegate,
			EligibilityServiceDelegate eligibilityServiceDelegate, Integer contractId,
			EligibilityInformationForm theForm, Contract currentContract) throws SystemException {

		// get the money types for the contract
		ContractServiceDelegate service = ContractServiceDelegate.getInstance();
		List<MoneyTypeVO> contractMoneyTypesVoList = service.getContractMoneyTypes(contractId, true);

		// get money types for the contract with EC on
		List<String> moneyTypes = eligibilityServiceDelegate.getMoneyTypesWithEligibilityService(contractId);

		// gets the list of PlanEntryRequirementDetailsVO for the contract
		Collection<PlanEntryRequirementDetailsVO> planEntryRequirementVOList = eligibilityServiceDelegate
				.getPlanEntryRequirementForPlan(contractId);
		theForm.setPlanMoneyTypeDetailsList(planEntryRequirementVOList);

		// gets the list of EmployeePlanEntryVO for the employee
		Collection<EmployeePlanEntryVO> employeePlanEntryVOList = eligibilityServiceDelegate
				.getEmployeePlanEntryDetailsForAllMoneyTypes(contractId, theForm.getProfileId());
		theForm.setEmployeeMoneyTypeDetailsList(employeePlanEntryVOList);

		// get the latest updated date for the money types
		Date latestInfoDate = getLatestUpdatedTimeStampForEmployeeMoneyType(employeePlanEntryVOList);
		theForm.setLatestInformationDate(latestInfoDate);

		// Evaluate Long Term Part Time Assessment
		theForm.setDisplayLongTermPartTimeAssessmentYearField(false);
		if(theForm.getEmployeeDetails().getEmployeeDetailVO().getHireDate() != null) {
			if(CensusConstants.EMPLOYMENT_STATUS_ACTIVE.equalsIgnoreCase(theForm.getEmployeeDetails().getEmployeeDetailVO().getEmploymentStatusCode()) 
					|| StringUtils.isBlank(theForm.getEmployeeDetails().getEmployeeDetailVO().getEmploymentStatusCode())) {
				if (theForm.getProfileId() != 0
						&& !(Constants.PRODUCT_RA457.equalsIgnoreCase(currentContract.getProductId()))) {
					int longTermPartTimeAssessmentYear = LongTermPartTimeAssessmentUtil.getInstance()
							.evaluateLongTermPartTimeAssessmentYear(contractId, theForm.getProfileId(), null);
	
					if (longTermPartTimeAssessmentYear >= EmployeeEligibilityAssessmentReqConstants.DEFAULT_LONG_TERM_PART_TIME_ASSESSMENT_YEAR) {
						theForm.setDisplayLongTermPartTimeAssessmentYearField(true);
						theForm.setLongTermPartTimeAssessmentYear(longTermPartTimeAssessmentYear);
					}
				}
			}
		}
		MoneyTypeVO moneyTypeVO = null;
		Date serviceElectionDate = null;
		List<MoneyTypeVO> moneyTypesDescList = new ArrayList<MoneyTypeVO>();
		Map<String, Date> serviceElectionDates = new HashMap<String, Date>();

		// populates the money details
		for (String moneyTypeId : moneyTypes) {
			moneyTypeId = StringUtils.trimToEmpty(moneyTypeId);

			// get MoneyTypeVO for money type id
			moneyTypeVO = getMoneyTypeDetailsVO(moneyTypeId, contractMoneyTypesVoList);

			// this is not contract money type
			// hence move to next money type
			if (moneyTypeVO == null) {
				continue;
			}
			moneyTypesDescList.add(moneyTypeVO);

			// get service election date for money type
			serviceElectionDate = eligibilityServiceDelegate
					.getEligibilityServiceSelectionDateForTheMoneyType(contractId, moneyTypeId);
			serviceElectionDates.put(moneyTypeId, serviceElectionDate);
		}

		// sort the money types based on alphabetical order of money source
		// short names
		Collections.sort(moneyTypesDescList, new Comparator<MoneyTypeVO>() {
			public int compare(MoneyTypeVO vo1, MoneyTypeVO vo2) {
				return vo1.getContractShortName().compareTo(vo2.getContractShortName());
			}
		});

		theForm.setMoneyTypeDescList(moneyTypesDescList);
		theForm.setServiceElectionDates(serviceElectionDates);
	}

	/*
	 * based on the request type makes a call to eligibility service to calculate
	 * the eligibility / PED calculation
	 * 
	 * @param eligibilityServiceDelegate
	 * 
	 * @param pendingRequestType
	 * 
	 * @param contractNumber
	 * 
	 * @param profileId
	 * 
	 * @param requestCauseTypeCode
	 * 
	 * @param sourceChannelCode
	 * 
	 * @param sourceFunctionCode
	 * 
	 * @param userIdType
	 * 
	 * @param userProfile
	 * 
	 * @throws SystemException
	 */
	private void calculateEligibility(EligibilityServiceDelegate eligibilityServiceDelegate,
			PENDING_REQUEST_TYPES pendingRequestType, int contractNumber, int profileId, String requestCauseTypeCode,
			String sourceChannelCode, String sourceFunctionCode, String userIdType, UserProfile userProfile)
			throws SystemException {

		RequestCauseTypeCode requestCause = RequestCauseTypeCode.fromCode(requestCauseTypeCode);
		SourceChannelCode sourceChannel = EligibilityRequestConstants.SourceChannelCode
				.getRealTimeSourceChannelCode(sourceChannelCode);
		SourceFunctionCode sourceFunction = SourceFunctionCode.fromCode(sourceFunctionCode);

		switch (pendingRequestType) {
		case EMPLOYEE_LEVEL_ELIGIBILITY_CALACULATION:
		case PLAN_LEVEL_ELIGIBILITY_CALACULATION:
			eligibilityServiceDelegate.calculateEligibilityForEmployee(contractNumber, profileId, null, requestCause,
					sourceChannel, sourceFunction, String.valueOf(userProfile.getPrincipal().getProfileId()),
					userIdType);
			break;
		case EMPLOYEE_LEVEL_PED_CALACULATION:
		case PLAN_LEVEL_PED_CALACULATION:
			eligibilityServiceDelegate.calculatePEDForEmployee(contractNumber, profileId, requestCause, sourceChannel,
					sourceFunction, String.valueOf(userProfile.getPrincipal().getProfileId()), userIdType);
			break;
		}

		// log to mrl
		logButtonClick(contractNumber, profileId, requestCauseTypeCode, sourceChannelCode, sourceFunctionCode,
				userProfile);
	}

	/*
	 * gets the latest eligibility request
	 * 
	 * @param pendingRequest
	 * 
	 * @return EligibilityRequestVO
	 */
	private EligibilityRequestVO getLatestPendingRequest(List<EligibilityRequestVO> pendingRequest) {

		int baseIndex = 0;
		if (pendingRequest.size() == 1) {
			return pendingRequest.get(baseIndex);
		}

		Collections.sort(pendingRequest, new Comparator<EligibilityRequestVO>() {
			public int compare(EligibilityRequestVO vo1, EligibilityRequestVO vo2) {
				return vo2.getLastUpdatedTime().compareTo(vo1.getLastUpdatedTime());
			}
		});
		return pendingRequest.get(baseIndex);
	}

	/*
	 * get the MoneyTypeVO for the given money type id
	 * 
	 * @param moneyTypeId
	 * 
	 * @param moneyTypeVOList
	 * 
	 * @return MoneyTypeVO
	 */
	private MoneyTypeVO getMoneyTypeDetailsVO(String moneyTypeId, Collection<MoneyTypeVO> moneyTypeVOList) {
		for (MoneyTypeVO moneyTypeVO : moneyTypeVOList) {
			if (StringUtils.equals(moneyTypeId, StringUtils.trimToEmpty(moneyTypeVO.getId()))) {
				return moneyTypeVO;
			}
		}
		return null;
	}

	/*
	 * calculates the age from dateOfBirth and sets to form bean
	 * 
	 * @param theForm
	 * 
	 * @param dateOfBirth
	 */
	private void calculateEmployeeAge(EligibilityInformationForm theForm, Date dateOfBirth) {
		if (dateOfBirth != null) {
			Calendar dob = Calendar.getInstance();
			dob.setTime(dateOfBirth);
			Calendar today = Calendar.getInstance();
			int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
			if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
				age--;
			}
			theForm.setEmployeeAge(Integer.toString(age));
		}
	}

	/*
	 * gets the latest updated time stamp for employee money types
	 * 
	 * @param employeeMoneyTypeDetails
	 * 
	 * @return Date
	 */
	private Date getLatestUpdatedTimeStampForEmployeeMoneyType(
			Collection<EmployeePlanEntryVO> employeeMoneyTypeDetails) {

		List<EmployeePlanEntryVO> list = new ArrayList<EmployeePlanEntryVO>(employeeMoneyTypeDetails);

		int baseIndex = 0;

		// if list empty return null;
		if (list.isEmpty()) {
			return null;
		}

		// if the list has only one element
		// return the the 1st element last updated time stamp
		if (list.size() == 1) {
			return list.get(baseIndex).getLastUpdatedTimeStamp();
		}

		// else sort the list in descending order
		// and then return the the 1st element last updated time stamp
		Collections.sort(list, new Comparator<EmployeePlanEntryVO>() {
			public int compare(EmployeePlanEntryVO vo1, EmployeePlanEntryVO vo2) {
				return vo2.getLastUpdatedTimeStamp().compareTo(vo1.getLastUpdatedTimeStamp());
			}
		});
		return list.get(baseIndex).getLastUpdatedTimeStamp();
	}

	/*
	 * logs the button click to mrl database
	 * 
	 * @param contractNumber
	 * 
	 * @param profileId
	 * 
	 * @param requestCauseTypeCode
	 * 
	 * @param sourceChannelCode
	 * 
	 * @param sourceFunctionCode
	 * 
	 * @param userProfile
	 */
	private void logButtonClick(int contractNumber, int profileId, String requestCauseTypeCode,
			String sourceChannelCode, String sourceFunctionCode, UserProfile userProfile) {

		StringBuffer logData = new StringBuffer();

		logData.append("contract id : ");
		logData.append(contractNumber);
		logData.append(" , ");
		logData.append("profile id : ");
		logData.append(profileId);
		logData.append(" , ");
		logData.append("request cause code : ");
		logData.append(requestCauseTypeCode);
		logData.append(" , ");
		logData.append("source channel code : ");
		logData.append(sourceChannelCode);
		logData.append(" , ");
		logData.append("source function code : ");
		logData.append(sourceFunctionCode);

		logWebActivity("Eligibility Info page button clicked", logData.toString(), userProfile, logger, interactionLog,
				logRecord);
	}

	/*
	 * log web activity
	 * 
	 * @param action
	 * 
	 * @param logData
	 * 
	 * @param profile
	 * 
	 * @param logger
	 * 
	 * @param interactionLog
	 * 
	 * @param logRecord
	 */
	private static void logWebActivity(String action, String logData, UserProfile profile, Logger logger,
			Category interactionLog, ServiceLogRecord logRecord) {
		try {
			ServiceLogRecord record = (ServiceLogRecord) logRecord.clone();
			record.setServiceName("EligibilityInformationAction");
			record.setMethodName(action);
			record.setData(logData);
			record.setDate(new Date());
			record.setPrincipalName(profile.getPrincipal().getUserName());
			record.setUserIdentity(
					profile.getPrincipal().getProfileId() + " : " + profile.getPrincipal().getUserName());
			interactionLog.error(record);
		} catch (CloneNotSupportedException e) {
			// log the error, but don't interrupt regular processing
			logger.error("error trying to log a button press for profile id [" + "] contract number [" + "]: " + e);
		}
	}

	/*
	 * gets the Plan Entry Dates for the contract
	 * 
	 * @param contractId
	 * 
	 * @param theForm
	 * 
	 * @return planEntryDates
	 * 
	 * @throws SystemException
	 */
	private List<Date> getPlanEntryDates(int contractId, EligibilityInformationForm theForm) throws SystemException {

		List<Date> planEntryDates = new ArrayList<Date>();

		// get plan data
		PlanData data = ContractServiceDelegate.getInstance().readPlanData(contractId);

		// get the plan year end
		DayOfYear planYearEnd = data.getPlanYearEnd();
		Calendar firstPlanYearEndDate = Calendar.getInstance();
		firstPlanYearEndDate.setTime(planYearEnd.getAsDateNonLeapYear());

		// get highest calculated eligibility date using HOS

		// Added as per CR9 - EEI.18.1
		Date highestCalculatedComputationPeriodEndDate = getHighestComputationDateUsingHOSCredtingMethod(
				theForm.getPlanMoneyTypeDetailsList(), theForm.getEmployeeMoneyTypeDetailsList());

		Calendar planYear = Calendar.getInstance();
		if (highestCalculatedComputationPeriodEndDate == null) {
			// if highest calculated eligibility date is null
			// set plan entry year to current year
			firstPlanYearEndDate.set(Calendar.YEAR, planYear.get(Calendar.YEAR));
		} else {
			// else set first plan year end to immediately
			// previous to highest eligibility date calculated using HOS
			Calendar computationDate = Calendar.getInstance();
			computationDate.setTime(highestCalculatedComputationPeriodEndDate);
			firstPlanYearEndDate.set(Calendar.YEAR, computationDate.get(Calendar.YEAR));

			while (firstPlanYearEndDate.getTime().compareTo(highestCalculatedComputationPeriodEndDate) < 0) {
				firstPlanYearEndDate.set(Calendar.YEAR, firstPlanYearEndDate.get(Calendar.YEAR) + 1);
			}
		}

		int maxNumberOfYears = 3;
		if (theForm.getEmployeeDetails().getEmployeeDetailVO().getHireDate() != null) {
			// if hire date is not null
			// calculate next two plan entry dates comparing with the hire date
			Calendar hireDate = Calendar.getInstance();
			hireDate.setTime(theForm.getEmployeeDetails().getEmployeeDetailVO().getHireDate());
			while (firstPlanYearEndDate.compareTo(hireDate) >= 0 && maxNumberOfYears > 0) {
				planEntryDates.add(firstPlanYearEndDate.getTime());
				firstPlanYearEndDate.add(Calendar.YEAR, -1);
				maxNumberOfYears--;
			}
		} else {
			// else directly calculate next 2 dates
			while (maxNumberOfYears > 0) {
				planEntryDates.add(firstPlanYearEndDate.getTime());
				firstPlanYearEndDate.add(Calendar.YEAR, -1);
				maxNumberOfYears--;
			}
		}

		return planEntryDates;
	}

	/*
	 * gets the plan YTD data for the employee
	 * 
	 * @param contractId
	 * 
	 * @param theForm
	 * 
	 * @param planEntryDates
	 * 
	 * @throws SystemException
	 */
	private void calculatePlanYTDData(EmployeeServiceDelegate employeeServiceDelegate, int contractId,
			EligibilityInformationForm theForm, List<Date> planEntryDates) throws SystemException {

		Map<String, LabelValueBean> planYtdData = new LinkedHashMap<String, LabelValueBean>();

		// get plan YTD hours and effective date
		Map<Date, Integer> planYTDHours = employeeServiceDelegate.getPlanYTDHours(contractId,
				new BigDecimal(theForm.getProfileId()));

		// get the plan YTD effective dates in sorted order
		Set<Date> effectiveDates = planYTDHours.keySet();
		List<Date> sortedEffectiveDates = new ArrayList<Date>(effectiveDates);
		Collections.sort(sortedEffectiveDates);
		Collections.reverse(sortedEffectiveDates);

		LabelValueBean value;
		Calendar contractEffectiveDate = Calendar.getInstance();
		contractEffectiveDate
				.setTime(ContractServiceDelegate.getInstance().readPlanData(contractId).getContractEffectiveDate());

		for (Date date : planEntryDates) {
			// get YTD hour and effective date for the plan entry date
			value = getPlanYTDDetails(date, planYTDHours, sortedEffectiveDates);
			// added for CR9 EEI.28
			Calendar planEntryDateCalendar = Calendar.getInstance();
			planEntryDateCalendar.setTime(date);

			if (value != null) {
				if ((planEntryDateCalendar.compareTo(contractEffectiveDate) <= 0) && value.getValue() != null
						&& !"".equals(value.getValue())) {
					planYtdData.put(DateRender.formatByPattern(date, "", MEDIUM_MDY_SLASHED), value);
				} else if (planEntryDateCalendar.compareTo(contractEffectiveDate) > 0) {
					planYtdData.put(DateRender.formatByPattern(date, "", MEDIUM_MDY_SLASHED), value);
				}
			}
		}

		theForm.setPlanYtdData(planYtdData);
	}

	/*
	 * get highest calculated Computation date EEI.18.1 CR09
	 * 
	 * @param planMoneyTypeDetailsList
	 * 
	 * @param employeeMoneyTypeDetailsList
	 * 
	 * @return highestComputationDate
	 */
	private Date getHighestComputationDateUsingHOSCredtingMethod(
			Collection<PlanEntryRequirementDetailsVO> planMoneyTypeDetailsList,
			Collection<EmployeePlanEntryVO> employeeMoneyTypeDetailsList) {

		Date highestComputationDate = null;
		List<String> moneyTypeIds = getMoneyTypeIdsCalculatedUsingHOSCreditingMethod(planMoneyTypeDetailsList);
		Collections.sort(moneyTypeIds);

		for (EmployeePlanEntryVO vo : employeeMoneyTypeDetailsList) {
			if (vo.getComputationPeriodEndDate() != null
					&& isMoneyTypeCalculatedUsingHOSCreditingMethod(moneyTypeIds, vo.getMoneyTypeId())) {
				if (highestComputationDate == null) {
					highestComputationDate = vo.getComputationPeriodEndDate();
				} else if (vo.getComputationPeriodEndDate().compareTo(highestComputationDate) >= 0) {
					highestComputationDate = vo.getComputationPeriodEndDate();
				}
			}
		}
		return highestComputationDate;
	}

	/*
	 * checks if money type is calculated using HOS
	 * 
	 * @param moneyTypeIdsCalculatedUsingHOSCreditingMethod
	 * 
	 * @param moneyTypeId
	 * 
	 * @return boolean
	 */
	private boolean isMoneyTypeCalculatedUsingHOSCreditingMethod(
			List<String> moneyTypeIdsCalculatedUsingHOSCreditingMethod, String moneyTypeId) {

		int value = Collections.binarySearch(moneyTypeIdsCalculatedUsingHOSCreditingMethod,
				StringUtils.trimToEmpty(moneyTypeId));
		return (value >= 0) ? true : false;
	}

	/*
	 * returns money ids calculated using HOS crediting method
	 * 
	 * @param planMoneyTypeDetailsList
	 * 
	 * @return List<String>
	 */
	private List<String> getMoneyTypeIdsCalculatedUsingHOSCreditingMethod(
			Collection<PlanEntryRequirementDetailsVO> planMoneyTypeDetailsList) {

		List<String> moneyTypeIds = new ArrayList<String>();
		for (PlanEntryRequirementDetailsVO vo : planMoneyTypeDetailsList) {
			if (SERVICE_CREDIT_METHOD_HOS.equals(vo.getServiceCreditMethodInd())) {
				moneyTypeIds.add(StringUtils.trimToEmpty(vo.getMoneyTypeId()));
			}
		}
		return moneyTypeIds;
	}

	/*
	 * get YTD hours and effective date for the the plan entry date
	 * 
	 * @param yearEndDate
	 * 
	 * @param planYTDHours
	 * 
	 * @param sortedEffectiveDates
	 * 
	 * @return LabelValueBean
	 */
	private LabelValueBean getPlanYTDDetails(Date yearEndDate, Map<Date, Integer> planYTDHours,
			List<Date> sortedEffectiveDates) {

		int ytdHours;
		LabelValueBean value = new LabelValueBean("", "");

		Calendar planYearEndDate = Calendar.getInstance();
		planYearEndDate.setTime(yearEndDate);

		Calendar planYearEndDateStartPeriod = Calendar.getInstance();
		planYearEndDateStartPeriod.setTime(yearEndDate);
		planYearEndDateStartPeriod.set(Calendar.YEAR, planYearEndDateStartPeriod.get(Calendar.YEAR) - 1);

		Calendar ytdEffectiveDate = Calendar.getInstance();
		for (Date date : sortedEffectiveDates) {
			ytdEffectiveDate.setTime(date);
			if (planYearEndDate.compareTo(ytdEffectiveDate) >= 0
					&& ytdEffectiveDate.compareTo(planYearEndDateStartPeriod) > 0) {
				ytdHours = planYTDHours.get(date);
				value = new LabelValueBean(DateRender.formatByPattern(date, "", MEDIUM_MDY_SLASHED),
						String.valueOf(ytdHours));
				break;
			}
		}

		return value;
	}

	@RequestMapping(value = "/eligibilityInformation/", params = { "task=printPDF" }, method = { RequestMethod.GET })
	public String doPrintPDF(@Valid @ModelAttribute("eligibilityInformationForm") EligibilityInformationForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if (StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("default");// if input
																											// forward
																											// not
																											// //available,
																											// provided
																											// default
			}
		}
		forward = super.doPrintPDF(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	/**
	 * This code has been changed and added to Validate form and request against
	 * penetration attack, prior to other validations.
	 */
	@Autowired
	private PSValidatorFWDefault psValidatorFWDefault;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWDefault);
	}

}
