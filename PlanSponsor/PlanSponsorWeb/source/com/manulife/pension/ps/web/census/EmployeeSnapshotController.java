package com.manulife.pension.ps.web.census;

import static com.manulife.util.render.RenderConstants.MEDIUM_MDY_SLASHED;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.EligibilityServiceDelegate;
import com.manulife.pension.delegate.EmployeeServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.ps.service.report.census.valueobject.CensusVestingDetails;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.census.util.EmployeeServiceFacade;
import com.manulife.pension.ps.web.census.util.EmployeeSnapshotErrorUtil;
import com.manulife.pension.ps.web.census.util.EmployeeSnapshotValidationErrors;
import com.manulife.pension.ps.web.census.util.ParameterizedActionForward;
import com.manulife.pension.ps.web.contract.csf.EligibilityCalculationMoneyType;
import com.manulife.pension.ps.web.controller.PsAutoController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.service.contract.util.PlanConstants;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.contract.valueobject.DayOfYear;
import com.manulife.pension.service.contract.valueobject.MoneyTypeVO;
import com.manulife.pension.service.contract.valueobject.PlanData;
import com.manulife.pension.service.eligibility.valueobject.EligibilityRequestVO;
import com.manulife.pension.service.eligibility.valueobject.EmployeePlanEntryVO;
import com.manulife.pension.service.eligibility.util.LongTermPartTimeAssessmentUtil;
import com.manulife.pension.service.employee.eligibility.constants.EmployeeEligibilityAssessmentReqConstants;
import com.manulife.pension.service.employee.util.EmployeeData;
import com.manulife.pension.service.employee.util.EmployeeData.Property;
import com.manulife.pension.service.employee.util.EmployeeValidationError;
import com.manulife.pension.service.employee.util.EmployeeValidationErrorCode;
import com.manulife.pension.service.employee.util.EmployeeValidationErrors;
import com.manulife.pension.service.employee.valueobject.AddressVO;
import com.manulife.pension.service.employee.valueobject.Employee;
import com.manulife.pension.service.employee.valueobject.EmployeeChangeHistoryVO;
import com.manulife.pension.service.employee.valueobject.EmployeeDetailVO;
import com.manulife.pension.service.employee.valueobject.EmployeeStatusInfo;
import com.manulife.pension.service.employee.valueobject.EmployeeVestingInfo;
import com.manulife.pension.service.employee.valueobject.EmployeeVestingVO;
import com.manulife.pension.service.employee.valueobject.ParticipantContractVO;
import com.manulife.pension.service.environment.valueobject.LabelValueBean;
import com.manulife.pension.service.plan.valueobject.PlanDataLite;
import com.manulife.pension.service.vesting.EmployeeVestingInformation;
import com.manulife.pension.service.vesting.VestingConstants;
import com.manulife.pension.service.vesting.util.VestingMessageType;
import com.manulife.pension.validator.ValidationError;
import com.manulife.pension.validator.ValidationError.Type;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.RenderConstants;

abstract public class EmployeeSnapshotController extends PsAutoController implements
		CensusConstants {
	// the source constants from where employee snapshot is being accessed
	private static final String AddressHistory = "addressHistory";

	private static final String AddressSummary = "addressSummary";
	private static final String CancelledParticipantStatus = "CN";

	private static final String EligibilitySummary = "eligibilitySummary";

	private static final String VestingSummary = "censusVesting";
    
    private static final String VestingInformation = "vestingInformation";
    public static final String EMPLOYEE_FORM = "editEmployeeForm";
	private static final String DeferralSource = "deferral";
	
	private static final String eligibilityInformation = "eligibilityInformation";
	
	private static final String TaskCenterSummary = "taskCenter";
    private static final String TaskCenterHistory = "taskCenterHistory";
    private static final String Deferral = "deferral";

	private static final String SUCCESS = "resetPassword";
    
    private static FastDateFormat eligibilityDateFormat = FastDateFormat.getInstance(
    		RenderConstants.MEDIUM_MDY_SLASHED, Locale.US);

	protected static EmployeeServiceFacade serviceFacade = new EmployeeServiceFacade();
	protected boolean displayFlag=false;
	protected EmployeeSnapshotController(Class clazz) {
		super(clazz);
	}

	 protected String doCommon(
			EmployeeSnapshotForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			SystemException {
		 //TODO
		return null;
	}

	/**
	 * Redirect the flow after the action decides the user can not access the
	 * functionality with the record
	 * 
	 * @return
	 */
	protected String redirectForNotAccessible() {
		//return mapping.findForward(Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT);
		return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
	}

	/**
	 * Do not allow external user to view/edit for employee has C employment
	 * status
	 * 
	 * @param employee
	 * @param user
	 * @return
	 */
	protected boolean isAccessibleForEmployee(Employee employee,
			UserProfile user) {
		ParticipantContractVO participant = employee.getParticipantContract();
		if (participant != null
				&& CancelledParticipantStatus.equals(participant
						.getParticipantStatusCode())) {
			return false;
		}
		if (EMPLOYMENT_STATUS_CANCEL.equals(employee.getEmployeeDetailVO()
				.getEmploymentStatusCode())
				&& !user.isInternalUser()) {
			return false;
		} else {
			return true;
		}
	}

	public String doShowPreviousValue(
			AutoForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			SystemException {

		UserProfile userProfile = SessionHelper.getUserProfile(request);
		EmployeeSnapshotForm form = (EmployeeSnapshotForm) actionForm;

		if (form.isShowCensusHistoryValue() != null) {
			userProfile.setShowCensusHistoryValue(form
					.isShowCensusHistoryValue().booleanValue());
		}

		setVestingSection(userProfile.getCurrentContract().getContractNumber(),
				form);
		
		return doCommon(form, request, response);
	}

	/**
	 * Initialize section expansion based on source. It could be externalized to
	 * a config file later....
	 * 
	 * @param source
	 * @param form
	 */
	protected void initializeSectionExpansion(EmployeeSnapshotForm form) {
			displayFlag=false;
		if (!form.isFromEdit() && !form.isFromView()) {
			displayFlag=true;
			String source = form.getSource();
			
			if(eligibilityInformation.equals(source)){
			    
			    	form.setExpandBasic(true);
				form.setExpandContact(false);
				form.setExpandEmployment(true);
				form.setExpandParticipation(true);
			
			}else if (AddressHistory.equals(source) || AddressSummary.equals(source)) {
				form.setExpandBasic(false);
				form.setExpandContact(true);
				form.setExpandEmployment(false);
				form.setExpandParticipation(false);
                form.setExpandVesting(false);
			} else if (EligibilitySummary.equals(source)) {
				form.setExpandBasic(true);
				form.setExpandContact(false);
				form.setExpandEmployment(true);
				form.setExpandParticipation(true);
                form.setExpandVesting(false);
			} else if (DeferralSource.equals(source) ||
					   TaskCenterSummary.equals(source) ||
					   TaskCenterHistory.equals(source)) {
				form.setExpandBasic(false);
				form.setExpandContact(false);
				form.setExpandEmployment(false);
				form.setExpandParticipation(true);
                form.setExpandVesting(false);				
			} else if (VestingSummary.equals(source)) {
				if (form.isShowVesting()) {
					form.setExpandBasic(false);
					form.setExpandContact(false);
					form.setExpandEmployment(true);
					form.setExpandParticipation(false);
                    form.setExpandVesting(true);			
				} else {
					form.setExpandBasic(true);
					form.setExpandContact(false);
					form.setExpandEmployment(false);
					form.setExpandParticipation(false);
                    form.setExpandVesting(false);
				}
            } else if (VestingInformation.equals(source)) {
                form.setExpandBasic(false);
                form.setExpandContact(false);
                form.setExpandEmployment(true);
                form.setExpandParticipation(false);
                form.setExpandVesting(true);    
			} else if (source.contains(EligibilitySummary)) {				
				form.setExpandContact(false);
				form.setExpandEmployment(true);
				form.setExpandParticipation(true);
                form.setExpandVesting(false);
                form.setExpandBasic(true);
			}else {
				form.setExpandBasic(true);
				form.setExpandContact(false);
				form.setExpandEmployment(false);
				form.setExpandParticipation(false);
                form.setExpandVesting(false);
			}
		}else if (VestingSummary.equals(form.getSource())) {
			if (form.isShowVesting()) {
				form.setExpandBasic(false);
				form.setExpandContact(false);
				form.setExpandEmployment(true);
				form.setExpandParticipation(false);
                form.setExpandVesting(true);			
			} else {
				form.setExpandBasic(true);
				form.setExpandContact(false);
				form.setExpandEmployment(false);
				form.setExpandParticipation(false);
                form.setExpandVesting(false);
			}
        }
			
		}


	/**
	 * Exit from the current page
	 * 
	 * @param mapping
	 * @param request
	 */
	protected void removeFormUponExit(HttpServletRequest request) {
		
		request.getSession(false).removeAttribute(EMPLOYEE_FORM);
	}

	protected String forwardToRealSource( 
			HttpServletRequest request, EmployeeSnapshotForm form) {
		removeFormUponExit( request);
		if (CensusConstants.PARTICIPANT_ACCOUNT_PAGE.equals(form.getSource())) {
			String configured = "redirect:/do/participant/participantAccount/";
			ParameterizedActionForward realForward = new ParameterizedActionForward(configured);
			realForward.addParameter("profileId", form.getProfileId());			
			return realForward.getPath();
        } else if (CensusConstants.VESTING_INFO_PAGE.equals(form.getSource())) {
            String configured = CensusConstants.VESTING_INFO_PAGE;
            ParameterizedActionForward realForward = new ParameterizedActionForward(
                    configured);
            realForward.addParameter("profileId", form.getProfileId());
            realForward.addParameter("source", CensusConstants.EMPLOYEE_SNAPSHOT_PAGE);
            return realForward.getPath();
		} else if (CensusConstants.ELIGIBILITY_INFO_PAGE.equals(form.getSource())) {
			String configured = CensusConstants.ELIGIBILITY_INFO_PAGE;
			ParameterizedActionForward realForward = new ParameterizedActionForward(
					configured);
			realForward.addParameter("profileId", form.getProfileId());
			return realForward.getPath();
		} 
		 else if (CensusConstants.SAMPLE_SOURCE.equals(form.getSource())) {
				String configured = "redirect:/do/census/employeeEnrollmentSummary";
				ParameterizedActionForward realForward = new ParameterizedActionForward(
						configured);
				return realForward.getPath();
			}
		
		else if (CensusConstants.RESET_PWD_PAGE.equals(form.getSource())) {
            String configured = "redirect:/do/pwdemail/ResetPassword/";
            ParameterizedActionForward realForward = new ParameterizedActionForward(configured);
            realForward.addParameter("profileId", form.getProfileId());
            realForward.addParameter("source", CensusConstants.CENSUS_SUMMARY_PAGE);
            return realForward.getPath();
		}
		else if (TaskCenterHistory.equals(form.getSource())) {
            String configured = "redirect:/do/participant/taskCenterHistory";
            ParameterizedActionForward realForward = new ParameterizedActionForward(configured);
            return realForward.getPath();
		}else {
			//return mapping.findForward(form.getSource());
			return form.getSource();
		}
	}

	protected String forwardToSource(
			HttpServletRequest request, EmployeeSnapshotForm form) {
		if(form.isFromReset()){
			return forwardToResetPIN( request, form);
		}
		if (form.isFromView()) {
			return forwardToViewEmployeeSnapshot( request, form);
		} else {
			removeFormUponExit( request);
			return forwardToRealSource( request, form);
		}
	}
	
	protected String forwardToResetPIN(
			HttpServletRequest request, EmployeeSnapshotForm form) {
		//ActionForward configured = mapping.findForward(SUCCESS);
		String configured = "redirect:/do/pwdemail/ResetPassword/";
		ParameterizedActionForward resetForward = new ParameterizedActionForward(configured);
		String profileId = form.getProfileId();
		
		resetForward.addParameter("profileId", profileId);
		if (form.getSource() != null) {
			resetForward.addParameter("source", form.getSource());
		}
		resetForward.addParameter("fromEdit", "true");
		resetForward.addParameter("showConfirmationToDo", "false");
		return resetForward.getPath();	
	}

	protected String forwardToViewEmployeeSnapshot(
			 HttpServletRequest request,
			EmployeeSnapshotForm form)  {
		removeFormUponExit( request);
		String configured = "redirect:/do/census/viewEmployeeSnapshot/";
		ParameterizedActionForward viewForward = new ParameterizedActionForward(configured);
		viewForward.addParameter("fromEdit", "true");
		viewForward.addParameter("profileId", form.getProfileId());
		viewForward.addParameter("source", form.getSource());
		viewForward.addParameter("expandBasic", Boolean.toString(form
				.isExpandBasic()&&!form.isShowConfirmationToDo()));
		viewForward.addParameter("expandEmployment", Boolean.toString(form
				.isExpandEmployment()&&!form.isShowConfirmationToDo()));
		viewForward.addParameter("expandContact", Boolean.toString(form
				.isExpandContact()&&!form.isShowConfirmationToDo()));
		viewForward.addParameter("expandParticipation", Boolean.toString(form
				.isExpandParticipation()&&!form.isShowConfirmationToDo()));
		viewForward.addParameter("expandVesting", Boolean.toString(form
				.isExpandVesting()&&!form.isShowConfirmationToDo()));
		viewForward.addParameter("showConfirmationToDo", Boolean.toString(form
                .isShowConfirmationToDo()));
		return viewForward.getPath();
	}
		
	protected void setErrors(EmployeeSnapshotForm form,
			HttpServletRequest request, List<ValidationError> errors) {
		if (errors != null && errors.size() > 0) {
			request.setAttribute("validationErrors",
					new EmployeeSnapshotValidationErrors(errors));
		}
		form.onErrors(errors);
		super.setErrorsInRequest(request, errors);
	}

	/**
	 * Validate the employee and put necessary errors/warnings in request for
	 * page to display.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param employee
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	protected void validateEmployee(
			EmployeeSnapshotForm form, HttpServletRequest request,
			Employee employee) throws SystemException {
		EmployeeValidationErrors bizErrors = serviceFacade.validate(employee,
				SessionHelper.getUserProfile(request), true, !form
						.isHosBasedVesting(), !form.isShowVesting());
		List<ValidationError> errors = new ArrayList<ValidationError>();
		if (!bizErrors.isEmpty()) {
			EmployeeSnapshotErrorUtil.getInstance().convertSkipRegularWarnings(
					bizErrors, errors);
		}
		addVestingErrors(errors, employee.getEmployeeVestingVO()
				.getEmployeeVestingInformation());
		if (!errors.isEmpty()) {
			setErrors(form, request, errors);
			form.expandOnErrors(errors);
		}
		
		EmployeeValidationError theError = EmployeeSnapshotErrorUtil
                .getInstance().getEmployeeValidationError(
                             bizErrors,
                             Property.EMPLOYER_PROVIDED_EMAIL,
                             EmployeeValidationErrorCode.DuplicateEmailAddress);
	   if( theError != null ){
	         EmployeeData employeedata = theError.getEmployees().get(0);
	         request.getSession(false).setAttribute(CensusConstants.EMPLOYEE_DUPLICATE_DATA,employeedata);
	  }

	}

	protected void addVestingErrors(List<ValidationError> errors,
			EmployeeVestingInformation evi) {
		if (evi != null) {
			Set<VestingMessageType> vestingErrors = evi.getErrors();
			if (vestingErrors != null) {
				if (vestingErrors
						.contains(VestingMessageType.VESTING_SCHEDULE_HAS_NOT_BEEN_SET_UP)) {
					errors.add(new ValidationError("",
							ErrorCodes.MISSING_VESTING_SCHEDULE, Type.warning));
				}
				if (vestingErrors
						.contains(VestingMessageType.CREDITING_METHOD_IS_UNSPECIFIED)) {
					errors.add(new ValidationError("",
							ErrorCodes.UNSPECIFIED_VESTING_CREDITING_METHOD,
							Type.warning));
				}
			}
		}
	}

	/**
	 * Get a UI-ready employee object. Since UI uses the nested property name to
	 * access property, set a default nested object if it is null in the
	 * original Employee object
	 * 
	 * @param employee
	 *            The business tier returned Employee object
	 * @return A Employee object based on the argument.
	 */
	protected Employee getEmployeeForUI(final Employee employee) {
		Employee employeeForUI;

		EmployeeDetailVO detail = employee.getEmployeeDetailVO();
		AddressVO address = employee.getAddressVO();
		EmployeeVestingVO vesting = employee.getEmployeeVestingVO();
		if (detail == null || address == null || vesting == null) {
			employeeForUI = new Employee(
					(detail == null ? new EmployeeDetailVO() : detail),
					(vesting == null ? new EmployeeVestingVO() : vesting),
					// may need to change, if field in this can be view/edit
					// employee.getEmployeeContract(),
					(address == null ? new AddressVO() : address));
			employeeForUI.setConfirmedIndicator(employee
					.getConfirmedIndicator());
			employeeForUI.setUserId(employee.getUserId());
			employeeForUI.setUserIdType(employee.getUserIdType());
			employeeForUI.setSpecialSortCategoryId(employee
					.getSpecialSortCategortId());
			employeeForUI.setParticipantContract(employee
					.getParticipantContract());
			employeeForUI.setParticipantAccountInd(employee
					.isParticipantAccountInd());
		} else {
			employeeForUI = employee;
		}
		return employeeForUI;
	}

	/**
	 * Check if the contract has EligibilityCalculationService is on/off
	 * 
	 */
	protected void setParticipationSection(int contractId, Employee employee,
			EmployeeSnapshotForm form,Contract currentContract) throws SystemException {

		try {
			Timestamp eligibilityLastInvokedTime = employee
					.getEmployeeDetailVO().getEligibilityLastInvokedDateTime();
			ContractServiceDelegate delegate = ContractServiceDelegate
					.getInstance();
			EmployeeServiceDelegate employeeServiceDelegate = EmployeeServiceDelegate
					.getInstance(Constants.PS_APPLICATION_ID);
			ContractServiceFeature eligibilityCalculationCSF = delegate
					.getContractServiceFeature(contractId,
							ServiceFeatureConstants.ELIGIBILITY_CALCULATION_CSF);
			if (eligibilityCalculationCSF != null
					&& ServiceFeatureConstants.YES
							.equals(eligibilityCalculationCSF.getValue())) {
				form.setEligibilityCalcCsfOn(true);

				// ESP 439 Pending eligibility calculation content
				if (form.getProfileId() != null) {
					EligibilityServiceDelegate eligibilityDelegate = EligibilityServiceDelegate
							.getInstance(Constants.PS_APPLICATION_ID);
					Collection<EligibilityRequestVO> eligibilityRequestVoCollection = eligibilityDelegate
							.getPendingPlanLevelOrEmployeeLevelRequests(
									contractId, Integer.parseInt(form.getProfileId()));
					if (eligibilityRequestVoCollection != null
							&& eligibilityRequestVoCollection.size() > 0) {
						Iterator<EligibilityRequestVO> eligibilityRequestVoIterator = eligibilityRequestVoCollection
								.iterator();
						while (eligibilityRequestVoIterator.hasNext()) {
							EligibilityRequestVO eligibilityRequestVo = (EligibilityRequestVO) eligibilityRequestVoIterator
									.next();
							Timestamp eligibiltyLastUpdatedDateTime = eligibilityRequestVo
									.getLastUpdatedTime();
							if (eligibiltyLastUpdatedDateTime instanceof Timestamp) {
								boolean validPendingRequest = false;
								if (eligibilityLastInvokedTime != null) {
									if (eligibiltyLastUpdatedDateTime
											.compareTo(eligibilityLastInvokedTime) >= 0) {
										validPendingRequest = true;
									}
								} else {
									validPendingRequest = true;
								}
								if (validPendingRequest) {
									form.setPendingEligibilityCalculationRequest(true);
									break;
								}
							}
						}
					}
				}// Fetch the eligible money types.
				ArrayList<EligibilityCalculationMoneyType> eligibilityCalculationMoneyTypeList = new ArrayList<EligibilityCalculationMoneyType>();

				// Compare the contract money types with all the money types
				// available for
				// Eligibility Calculation service feature

				Integer contractIdInt = new Integer(contractId);
				// It gets the moneyType for a given contract.
				List<MoneyTypeVO> contractMoneyTypesVoList = delegate
						.getContractMoneyTypes(contractIdInt, true);
				Map<String, MoneyTypeVO> map = new TreeMap<String, MoneyTypeVO> ();
				for(MoneyTypeVO vo: contractMoneyTypesVoList){
				    map.put(vo.getContractShortName(), vo);
				}
				
				
				// It gets all the eligible money types that can have
				// EligiblityCalculationService feature
				Collection eligibleServiceMoneyTypesList = eligibilityCalculationCSF
						.getAttributeNames();
				Iterator contractMoneyTypeIterator = map.values().iterator();
				while (contractMoneyTypeIterator.hasNext()) {
					MoneyTypeVO moneyTypeVo = (MoneyTypeVO) contractMoneyTypeIterator
							.next();
					String contractMoneyType = moneyTypeVo.getId();
					Iterator eligibleServiceMoneyTypesIterator = eligibleServiceMoneyTypesList
							.iterator();
					while (eligibleServiceMoneyTypesIterator.hasNext()) {
						String eligibleServiceMoneyType = (String) eligibleServiceMoneyTypesIterator
								.next();
						if (StringUtils.equals(eligibleServiceMoneyType,
								contractMoneyType)
								&& (ServiceFeatureConstants.YES
										.equalsIgnoreCase(eligibilityCalculationCSF
												.getAttributeValue(contractMoneyType)))) {
							EligibilityCalculationMoneyType eligibiltyCalculationMoneyType = new EligibilityCalculationMoneyType();
							// Set Attribute name
							eligibiltyCalculationMoneyType.setMoneyTypeId(contractMoneyType);
							eligibiltyCalculationMoneyType.setMoneyTypeName(moneyTypeVo.getContractShortName());
							if (form.getProfileId() != null) {
								int profileId = Integer.parseInt(form
										.getProfileId());
								// Set EligibilityDate and CalculationOverride indicator
								EmployeePlanEntryVO employeePlanEntryVo = EligibilityServiceDelegate
										.getInstance(
												Constants.PS_APPLICATION_ID)
										.getEmployeePlanEntryDetails(
												contractId, profileId,
												contractMoneyType);
								if (employeePlanEntryVo != null) {
									if (employeePlanEntryVo.getEligibilityDate() != null) {
										employee.setEligibilityDateByMoneyTypePresent(true);
										eligibiltyCalculationMoneyType
												.setEligibilityDate(eligibilityDateFormat
														.format(employeePlanEntryVo.getEligibilityDate()));
									}
									eligibiltyCalculationMoneyType
											.setCalculationOverride(employeePlanEntryVo
													.getEligibilityProvidedIndicator());
								}
							}
							eligibilityCalculationMoneyTypeList
									.add(eligibiltyCalculationMoneyType);
							break;
						}
					}
				}
				form.setEligibilityServiceMoneyTypes(eligibilityCalculationMoneyTypeList);
				form.setLastUpdatedEligibilityServiceMoneyTypes(copyLastUpdatedEligibityServiceMoneyTypes(eligibilityCalculationMoneyTypeList));
				
				// Evaluate Long Term Part Time Assessment
				if(validateDisplayLongTermPartTermAssessYearField(contractId,form,eligibilityCalculationCSF,employee,currentContract)) {
					int longTermPartTimeAssessmentYear = LongTermPartTimeAssessmentUtil.getInstance()
							.evaluateLongTermPartTimeAssessmentYear(contractId, Integer.parseInt(form.getProfileId()),
									null);

					if (longTermPartTimeAssessmentYear >= EmployeeEligibilityAssessmentReqConstants.DEFAULT_LONG_TERM_PART_TIME_ASSESSMENT_YEAR) {
						form.setDisplayLongTermPartTimeAssessmentYearField(true);
						form.setLongTermPartTimeAssessmentYear(longTermPartTimeAssessmentYear);
					} else {
						form.setDisplayLongTermPartTimeAssessmentYearField(false);
					}
				} else {
					form.setDisplayLongTermPartTimeAssessmentYearField(false);
				}
			} else {
				form.setEligibilityCalcCsfOn(false);
			}
		} catch (ApplicationException exception) {
			throw new SystemException(exception,
					"Error thrown while retreiving  EC csf");
		}
	}
	
	/**
	 * This method to check if the contract has EligibilityCalculationService is on/off
	 * and employee status and RA457 plan
	 * 
	 * @param form
	 * @param employee
	 * @param currentContract
	 * @return boolean
	 */
	private boolean validateDisplayLongTermPartTermAssessYearField(int contractId,EmployeeSnapshotForm form,ContractServiceFeature eligibilityCalculationCSF,Employee employee,Contract currentContract){
		
		boolean displayLongTermPartTermAssessYearField=false;
		
		if (eligibilityCalculationCSF != null
				&& ServiceFeatureConstants.YES.equals(eligibilityCalculationCSF.getValue())
				&& form.getProfileId() != null && !(Constants.PRODUCT_RA457.equalsIgnoreCase(currentContract.getProductId())) 
				&& employee!=null && employee.getEmployeeDetailVO()!=null
				&& employee.getEmployeeDetailVO().getHireDate()!=null
				&& (CensusConstants.EMPLOYMENT_STATUS_ACTIVE.equalsIgnoreCase(employee.getEmployeeDetailVO().getEmploymentStatusCode()) 
				|| StringUtils.isBlank(employee.getEmployeeDetailVO().getEmploymentStatusCode()))){
			
			displayLongTermPartTermAssessYearField=true;
		}
		return displayLongTermPartTermAssessYearField;
	}

	/**
	 * This method keeps a copy of last updated eligibility service money types,
	 * which is used to compare and check if the user has edited any value or
	 * not.
	 * 
	 * @param lastUpdatedEligibilityServiceMoneyTypeList
	 * @return ArrayList
	 */
	private ArrayList<EligibilityCalculationMoneyType> copyLastUpdatedEligibityServiceMoneyTypes(
			ArrayList<EligibilityCalculationMoneyType> lastUpdatedEligibilityServiceMoneyTypeList) {
		ArrayList<EligibilityCalculationMoneyType> cloneEligibilityServiceMoneyTypeList = new ArrayList<EligibilityCalculationMoneyType>();
		Iterator<EligibilityCalculationMoneyType> lastUpdatedListIterator = lastUpdatedEligibilityServiceMoneyTypeList
				.iterator();
		while (lastUpdatedListIterator.hasNext()) {
			EligibilityCalculationMoneyType eligibilityCalculationMoneyType = new EligibilityCalculationMoneyType();
			EligibilityCalculationMoneyType lastUpdated = (EligibilityCalculationMoneyType) lastUpdatedListIterator
					.next();
			eligibilityCalculationMoneyType.setMoneyTypeDescription(lastUpdated
					.getMoneyTypeDescription());
			eligibilityCalculationMoneyType.setMoneyTypeName(lastUpdated
					.getMoneyTypeName());
			eligibilityCalculationMoneyType.setMoneyTypeId(lastUpdated
					.getMoneyTypeId());
			eligibilityCalculationMoneyType.setMoneyTypeValue(lastUpdated
					.getMoneyTypeValue());
			eligibilityCalculationMoneyType.setEligibilityDate(lastUpdated.getEligibilityDate());
			eligibilityCalculationMoneyType.setCalculationOverride(lastUpdated.getCalculationOverride());
			cloneEligibilityServiceMoneyTypeList
					.add(eligibilityCalculationMoneyType);
		}
		return cloneEligibilityServiceMoneyTypeList;
	}
	
	protected void setVestingSection(int contractNumber,
			EmployeeSnapshotForm form) throws SystemException {
		try {
			boolean showVesting = false;
			boolean hosBasedVesting = false;
			boolean vestingCollected = false;
			ContractServiceFeature csf = ContractServiceDelegate
					.getInstance()
					.getContractServiceFeature(
							contractNumber,
							ServiceFeatureConstants.VESTING_PERCENTAGE_FEATURE);
            final PlanDataLite planDataLite = ContractServiceDelegate.getInstance()
                           .getPlanDataLight(new Integer(contractNumber));
			if (ServiceFeatureConstants.CALCULATED
					.equals(csf.getValue())) {
	            final String creditingMethod = planDataLite.getVestingServiceCreditMethod();
				if (VestingConstants.CreditingMethod.HOURS_OF_SERVICE
						.equals(creditingMethod)) {
					showVesting = true;
					hosBasedVesting = true;
				} else if (VestingConstants.CreditingMethod.ELAPSED_TIME
						.equals(creditingMethod)) {
					showVesting = true;
				}
			} else if (ServiceFeatureConstants.PROVIDED.equals(csf.getValue())) {
				vestingCollected = true;
                showVesting = true;
			}
			form.setShowVesting(showVesting);
			form.setHosBasedVesting(hosBasedVesting);
			form.setVestingCollected(vestingCollected);
            
			if (showVesting) {
		        final Map<String, Collection> lookupData = ContractServiceDelegate.getInstance().getLookupData(contractNumber);
		        final List<MoneyTypeVO> list = (List<MoneyTypeVO>)lookupData.get(PlanConstants.MONEY_TYPES_BY_CONTRACT);
				form.setMoneyTypes(list);
			}
			// plan year end only needed for HOS based.
			if (hosBasedVesting) {
                form.setPlanYearEnd(planDataLite.getPlanYearEnd().getAsDate());
            }

		} catch (ApplicationException e) {
			throw new SystemException(e,
					"Fail to get contract service feature of Vesting percentage: "
							+ contractNumber);
		}
	}
	
	
	/**
     * Initialize section expansion based on source. It could be externalized to a config file
     * later....
     * 
     * @param source
     * @param form
     */
    protected void initializeForm(EmployeeSnapshotForm form) {
        if (!form.isFromEdit() && !form.isFromView()) {
            String source = form.getSource();
            if (AddressHistory.equals(source) || AddressSummary.equals(source)) {
                form.setExpandBasic(false);
                form.setExpandContact(true);
                form.setExpandEmployment(false);
                form.setExpandParticipation(false);
            } else if (EligibilitySummary.equals(source)) {
                form.setExpandBasic(true);
                form.setExpandContact(false);
                form.setExpandEmployment(false);
                form.setExpandParticipation(true);     
            } else if (TaskCenterSummary.equals(source) || 
            		   TaskCenterHistory.equals(source) || Deferral.equals(source)) {
                form.setExpandBasic(false);
                form.setExpandContact(false);
                form.setExpandEmployment(false);
                form.setExpandParticipation(true);                 	
            } else {
                form.setExpandBasic(true);
                form.setExpandContact(false);
                form.setExpandEmployment(false);
                form.setExpandParticipation(false);
            }
        }
    }

    /**
     * We lookup the participant status and set a flag in the form if the participant has a partial
     * status.
     * 
     * @param employee The employee to get the status from.
     * @param employeeSnapshotForm The form to set the flag on.
     */
    protected void checkPartialParticipantStatus(final Employee employee,
            final EmployeeSnapshotForm employeeSnapshotForm) {

        final ParticipantContractVO participantContractVo = employee.getParticipantContract();
        if (participantContractVo != null) {
            final String participantStatusCode = participantContractVo.getParticipantStatusCode();

            if (CensusVestingDetails.isParticipantStatusPartial(participantStatusCode)) {
                // The status code is a partial code.
                employeeSnapshotForm.setPartialParticipantStatus(Boolean.TRUE);
            } else {
                employeeSnapshotForm.setPartialParticipantStatus(Boolean.FALSE);
            } // fi
        } // fi
    }
    
    protected void updatePreviousStatusFromParamTable(EmployeeHistoryView history, EmployeeVestingInfo statusParamInfo) {
        
        EmployeeChangeHistoryVO statusChangeVO = history.getPropertyMap().get("employmentStatus");
        if (statusChangeVO == null) {
            statusChangeVO = new EmployeeChangeHistoryVO();
        }
        
        EmployeeChangeHistoryVO statusDateChangeVO = history.getPropertyMap().get("employmentStatusEffectiveDate");
        if (statusDateChangeVO == null) {
            statusDateChangeVO = new EmployeeChangeHistoryVO();
        }
        
        if (statusParamInfo.getPreviousStatus() != null) {
            EmployeeStatusInfo info = statusParamInfo.getPreviousStatus();
            statusChangeVO.setPreviousValue(info.getStatus());
            if (info.getEffectiveDate() != null) {
                statusDateChangeVO.setPreviousValue(info.getEffectiveDate().toString());
            } 
        } else {
            statusChangeVO.setPreviousValue("");
            statusDateChangeVO.setPreviousValue("");
        }
        
        
        history.getPropertyMap().put("employmentStatus",statusChangeVO);
        history.getPropertyMap().put("employmentStatusEffectiveDate",statusDateChangeVO);
        
    }    
    
    /**
     * This method updates the history of eligibility date by ECservice money type.
     * @param historyList
     * @param history
     * @param form
     */
    protected void updateEligibilityDateHistory(List historyList,EmployeeHistoryView history,EmployeeSnapshotForm form){
    	
    	int totalNumberOfMoneyTypes = form.getEligibilityServiceMoneyTypes()
		.size();
        String eligibilityDateColumn="ELIGIBILITY_DATE";
        String calculationOverrideColumn="PROVIDED_ELIGIBILITY_DATE_IND";
        String propertyNameFirstPart="eligibityServiceMoneyTypeId[";
        String eligibilityDateProperty="].eligibilityDate";
        String calculationOverrideProperty="].calculationOverride";
        
       Iterator historyIterator=historyList.iterator();
         while(historyIterator.hasNext()){
        	EmployeeChangeHistoryVO employeeChangeHistoryVO=(EmployeeChangeHistoryVO)historyIterator.next();
        	String columnName=employeeChangeHistoryVO.getColumnName();
        	int index=columnName.indexOf("_", 0);
        	String historyMoneyType=columnName.substring(0,index);
        	String actualColumnName=columnName.substring(++index,columnName.length());
        	for (int i = 0; i < totalNumberOfMoneyTypes; i++) {
        		EligibilityCalculationMoneyType eligibilityMoneyType = form
        				.getEligibilityServiceMoneyTypes().get(i);
        		
        	if(historyMoneyType.equalsIgnoreCase(eligibilityMoneyType.getMoneyTypeId())){
        		Map propertyMap=history.getPropertyMap();
        		if(eligibilityDateColumn.equalsIgnoreCase(actualColumnName)){
        		propertyMap.put(propertyNameFirstPart+ i + eligibilityDateProperty, employeeChangeHistoryVO);
        		}else if(calculationOverrideColumn.equalsIgnoreCase(actualColumnName)){
        		propertyMap.put(propertyNameFirstPart + i
								+ calculationOverrideProperty,employeeChangeHistoryVO);
					}
				}
			}
		}
         
		// adding the Eligibility LTPT Assessment Year History details
		Optional<EmployeeChangeHistoryVO> optianl = historyList.stream().filter(vo -> (((EmployeeChangeHistoryVO) vo)
				.getColumnName().contains("PART_TIME_QUALIFICATION_YEAR")
				&& !(((EmployeeChangeHistoryVO) vo).getColumnName().contains("PREVIOUS_PART_TIME_QUALIFICATION_YEAR"))))
				.findFirst();

		if (optianl.isPresent()) {
			history.getPropertyMap().put("partTimeQulicationYr", optianl.get());
		}
	}
    /**
     * Filter out Canceled statuses for external users
     * 
     * @param userProfile
     * @param info
     * @return EmployeeVestingInfo
     */
    protected EmployeeVestingInfo filterOutCanceledStatus(UserProfile userProfile, EmployeeVestingInfo info) {
        EmployeeVestingInfo newInfo = new EmployeeVestingInfo(info.getProfileId(), info.getContractId());
        if (userProfile.getRole().isExternalUser()) {
            for (EmployeeStatusInfo statusInfo : info.getFullStatusList()) {
                if (!statusInfo.getStatus().equals(CensusConstants.EMPLOYMENT_STATUS_CANCEL)) {
                    newInfo.addEmploymentStatus(statusInfo);
                }
            }
            return newInfo;
        }
        return info;
    }
    
    /**
     * To Get Latest 3 Plan Entry Dates 
     * @param info
     * @return planEntryDates
     * @throws SystemException
     */
    private List<java.sql.Date> getLatestPlanYearEnds(EmployeeVestingInfo info)
			throws SystemException {
		List<java.sql.Date> planEntryDates = new ArrayList<java.sql.Date>();
		PlanData data = ContractServiceDelegate.getInstance().readPlanData(
				info.getContractId());
		DayOfYear planYearEnd = data.getPlanYearEnd();
		Calendar firstPlanYearEndDate = Calendar.getInstance();
		
		// -1 is added to give month in 0 to 11 numbers
		firstPlanYearEndDate.set(Calendar.MONTH, planYearEnd.getMonth() - 1);
		firstPlanYearEndDate.set(Calendar.DAY_OF_MONTH, planYearEnd.getDay());
		
		Calendar currentDate = Calendar.getInstance();

		if (firstPlanYearEndDate.compareTo(currentDate) < 0) {
			firstPlanYearEndDate.set(Calendar.YEAR, firstPlanYearEndDate
					.get(Calendar.YEAR) + 1);
		}
		
		for (int maxNumberOfYears = 3; maxNumberOfYears > 0; maxNumberOfYears--) {
			planEntryDates.add(new java.sql.Date(firstPlanYearEndDate.getTimeInMillis()));
			firstPlanYearEndDate.add(Calendar.YEAR, -1);
		}

		Collections.sort(planEntryDates);
		Collections.reverse(planEntryDates);
		return planEntryDates;
	}

	/**
	 * To Get PlanYTD data which contains ytd hours worked,effective date, and plan entry date
	 * @param info
	 * @return planYtdData
	 * @throws SystemException
	 */
	@SuppressWarnings("unchecked")
	protected Map<String, LabelValueBean> getPlanYTDHoursData(
			EmployeeVestingInfo info) throws SystemException {
		List<java.sql.Date> planEntryDates = getLatestPlanYearEnds(info);
		Map<String, LabelValueBean> planYtdData = new LinkedHashMap<String, LabelValueBean>();
		EmployeeServiceDelegate employeeServiceDelegate = EmployeeServiceDelegate
				.getInstance("PS");

		Map<Date, Integer> planYTDHours = employeeServiceDelegate
				.getPlanYTDHours(info.getContractId().intValue(),
						new BigDecimal(info.getProfileId().longValue()));
		Set<Date> effectiveDates = planYTDHours.keySet();
		List<Date> sortedEffectiveDates = new ArrayList<Date>(effectiveDates);
		Comparator comp = Collections.reverseOrder();
		Collections.sort(sortedEffectiveDates,comp);
		//Collections.reverse(sortedEffectiveDates);
		LabelValueBean value;
		// get contract effective date
		Date contractEffectiveDate = ContractServiceDelegate.getInstance()
				.readPlanData(info.getContractId()).getContractEffectiveDate();

		for (java.sql.Date date : planEntryDates) {
			if (date != null && contractEffectiveDate != null) {
				value = getPlanYTDDetails(date, planYTDHours,
						sortedEffectiveDates);
								
				if (value != null) {
					// added for => do not add plan year end date if YTD hours &
					// effective date is blank and
					// plan year end date is on or before contract effective
					// date
					
					String planEntryDateString = new SimpleDateFormat("yyyy-MM-dd").format(date);
					String contractEffectiveDateString = new SimpleDateFormat("yyyy-MM-dd").format(contractEffectiveDate);
				
					java.sql.Date planEntryDate= java.sql.Date.valueOf(planEntryDateString);
					java.sql.Date contractEffectiveDT= java.sql.Date.valueOf(contractEffectiveDateString);
					
					if (!(value.getLabel().equals("")
							&& value.getValue().equals("")
							&& planEntryDate.compareTo(contractEffectiveDT)<=0)){
						planYtdData.put(DateRender.formatByPattern(date, "",
								"MM/dd/yyyy"), value);
					}
				}
			}
		}
		return planYtdData;
	}

	/**
	 * To get YTD Hours worked and effective date wrapped in LabelValueBean Object
	 * @param yearEndDate
	 * @param planYTDHours
	 * @param sortedEffectiveDates
	 * @return value
	 */
	private LabelValueBean getPlanYTDDetails(Date yearEndDate,
			Map<Date, Integer> planYTDHours, List<Date> sortedEffectiveDates) {
		int ytdHours;
		LabelValueBean value = new LabelValueBean("", "");
		Calendar planYearEndDate = Calendar.getInstance();
		planYearEndDate.setTime(yearEndDate);
		Calendar planYearEndDateStartPeriod = Calendar.getInstance();
		planYearEndDateStartPeriod.setTime(yearEndDate);
		planYearEndDateStartPeriod.set(Calendar.YEAR,
				planYearEndDateStartPeriod.get(Calendar.YEAR) - 1);

		Calendar ytdEffectiveDate = Calendar.getInstance();
		for (Date date : sortedEffectiveDates) {
			ytdEffectiveDate.setTime(date);
			if (planYearEndDate.compareTo(ytdEffectiveDate) >= 0
					&& ytdEffectiveDate.compareTo(planYearEndDateStartPeriod) > 0) {
				ytdHours = planYTDHours.get(date);
				value = new LabelValueBean(DateRender.formatByPattern(date, "",
						MEDIUM_MDY_SLASHED), String.valueOf(ytdHours));
				break;
			}
		}
		return value;
	}
}