package com.manulife.pension.ps.web.census;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

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
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.EligibilityServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.census.EditEmployeeSnapshotForm.PageErrorType;
import com.manulife.pension.ps.web.census.util.CensusErrorCodes;
import com.manulife.pension.ps.web.census.util.EmployeeSnapshotErrorUtil;
import com.manulife.pension.ps.web.census.util.EmployeeSnapshotSecurityProfile;
import com.manulife.pension.ps.web.contract.csf.EligibilityCalculationMoneyType;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWError;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.eligibility.EligibilityRequestConstants.PartTimeQulificationYear;
import com.manulife.pension.service.eligibility.util.LongTermPartTimeAssessmentUtil;
import com.manulife.pension.service.eligibility.valueobject.EmployeePlanEntryVO;
import com.manulife.pension.service.employee.EmployeeValidationException;
import com.manulife.pension.service.employee.eligibility.constants.EmployeeEligibilityAssessmentReqConstants;
import com.manulife.pension.service.employee.util.EmployeeData;
import com.manulife.pension.service.employee.util.EmployeeData.Property;
import com.manulife.pension.service.employee.util.EmployeeValidationError;
import com.manulife.pension.service.employee.util.EmployeeValidationError.ErrorType;
import com.manulife.pension.service.employee.util.EmployeeValidationErrorCode;
import com.manulife.pension.service.employee.util.EmployeeValidationErrors;
import com.manulife.pension.service.employee.valueobject.Employee;
import com.manulife.pension.service.employee.valueobject.EmployeeDetailVO;
import com.manulife.pension.service.employee.valueobject.EmployeeStatusInfo;
import com.manulife.pension.service.employee.valueobject.EmployeeVestingInfo;
import com.manulife.pension.service.employee.valueobject.EmployeeVestingInfo.VestingType;
import com.manulife.pension.service.employee.valueobject.EmployeeVestingVO;
import com.manulife.pension.service.employee.valueobject.UserIdType;
import com.manulife.pension.service.plan.valueobject.MoneyTypeEligibilityCriterion;
import com.manulife.pension.service.security.role.ExternalUser;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.validator.ValidationError;
import com.manulife.pension.validator.ValidationError.Type;
import com.manulife.util.render.RenderConstants;

/**
 *
 * Edit employeesnapshot action.
 *
 * Support save, reset and cancel.
 *
 * @author guweigu
 */
@Controller
@RequestMapping(value ="/census")
@SessionAttributes({"editEmployeeForm"})

public class EditEmployeeSnapshotController extends EmployeeSnapshotController {
	@ModelAttribute("editEmployeeForm") 
	public EditEmployeeSnapshotForm populateForm()
	{
		return new EditEmployeeSnapshotForm();
	}
	
public static HashMap<String,String> forwards = new HashMap<String,String>();
	
	static{
		forwards.put("input","/census/editEmployeeSnapshot.jsp");
		forwards.put("resetPassword","redirect:/do/pwdemail/ResetPassword/");
        forwards.put("editEmployeeSnapshot","/census/editEmployeeSnapshot.jsp");
        forwards.put("viewEmployeeSnapshot","redirect:/do/census/viewEmployeeSnapshot/");
        forwards.put("censusSummary","redirect:/do/census/censusSummary/");
        forwards.put("addressHistory","redirect:/do/participant/addressHistory");
        forwards.put("eligibilitySummary","redirect:/do/census/employeeEnrollmentSummary");            
        forwards.put("addressSummary","redirect:/do/participant/participantAddresses");
        forwards.put("participantAccount","redirect:/do/participant/participantAccount/");
        forwards.put("taskCenter","redirect:/do/participant/taskCenterTasks");
        forwards.put("taskCenterHistory","redirect:/do/participant/taskCenterHistory");           
        forwards.put("deferral","redirect:/do/census/deferral");           
        forwards.put("censusVesting","redirect:/do/census/censusVesting/");
        forwards.put("employeeStatusHistory","redirect:/do/census/employeeStatusHistory/");
        forwards.put("error","/census/editEmployeeSnapshot.jsp");
        forwards.put("payrollSelfService","redirect:/do/participant/payrollSelfService/");
        forwards.put("challengePasscode", "redirect:/do/passcodeTransition/");
	}
	
	
	protected static final String SSNWarningAttribute = "ssnWarnings";

	protected static final String SSNWarningMessage = "ssnWarningMsg";
	private static final FastDateFormat eligibilityDateFormat = FastDateFormat.getInstance("MM/dd/yyyy",Locale.US);
	private static final FastDateFormat dateFormat = FastDateFormat.getInstance("MM/dd/yyyy",Locale.US);
	
	

	protected static final ValidationError ssnWarningValidationError = new ValidationError(
			EmployeeSnapshotProperties.SSN, CensusErrorCodes.SimilarSSN,
			Type.warning);
	
	protected static final ValidationError eligibilityDateWarningPpt = new ValidationError(
			EmployeeSnapshotProperties.EligibilityDate, CensusErrorCodes.MissingEligibilityDateForPpt,
			Type.warning);
	
	protected static final ValidationError eligibilityDateWarningNonPpt = new ValidationError(
			EmployeeSnapshotProperties.EligibilityDate, CensusErrorCodes.MissingEligibilityDateForNonPpt,
			Type.warning);
	
	protected static final String ELIGIBILITY_MONEY_TYPE_LIST_ATTRIBUTE="eligbilityMoneyTypeList";
	
	protected static enum State {
        Normal, Error, SimilarSsnError, Warning,
    };

    public EditEmployeeSnapshotController() {
        super(EditEmployeeSnapshotController.class);
    }

    /**
     * Common entry for forward back to the input page. 1. Setting up view salary permission for the
     * form 2. Load employee change history 3. Set readonly indicator for input page
     *
     * @param mapping
     * @param request
     * @param form
     * @param state
     * @return
     * @throws SystemException
     */
    protected String forwardToInput(HttpServletRequest request,
            EditEmployeeSnapshotForm form, State state) throws SystemException {
        UserProfile userProfile = getUserProfile(request);
        EmployeeSnapshotSecurityProfile profile = new EmployeeSnapshotSecurityProfile(userProfile);
        // set the view salary permission
        form.setViewSalary(profile.isViewSalary(form
                .getValue(EmployeeSnapshotProperties.MaskSensitiveInformation)));
        // save the values for comparison for next save
        form.saveInputValues();

        switch (state) {
            case Normal:
                form.setReadOnly(false);
                break;
            case Error:
                form.setReadOnly(false);
                break;
            case Warning:
            {
                form.setReadOnly(true);
               // form.setOptOutDisabled(true);
               // form.setAe90DaysOptOutDisabled(true);
                break;
            }
            case SimilarSsnError: 
            {
                form.setReadOnly(true);
               // form.setOptOutDisabled(true);
               // form.setAe90DaysOptOutDisabled(true);
                break;
            }
        }
        
        if (form.getProfileId() != null) {
                         
            // get employment status history
            EmployeeVestingInfo statusParamInfo = serviceFacade.getEmployeeVestingInfo(Long.parseLong(form.getProfileId()), 
                        userProfile.getCurrentContract().getContractNumber(), 
                        new Date(), VestingType.EMPLOYMENT_STATUS, true, true, false);
            EmployeeVestingInfo filteredStatusInfo = filterOutCanceledStatus(userProfile, statusParamInfo);
            request.setAttribute(CensusConstants.EMPLOYEE_STATUS_HISTORY_ATTRIBUTE, filteredStatusInfo);
            
            // get employee change history
            List historyList = serviceFacade.getEmployeeChangeHistory(
                                Long.parseLong(form.getProfileId()), userProfile);
            EmployeeHistoryView history = new EmployeeHistoryView(historyList);
            
            //updates the history of eligibility date by ECservice money type.
            updateEligibilityDateHistory(historyList,history,form);
            // update previous values from the employee_vesting_parameter table
            updatePreviousStatusFromParamTable(history, filteredStatusInfo);
            request.setAttribute(CensusConstants.EMPLOYEE_HISTORY_ATTRIBUTE, history);
            
            // get plan entry data & YTD Hours,effective date
   			Map data = getPlanYTDHoursData(statusParamInfo);
               request.setAttribute(CensusConstants.EMPLOYEE_PLAN_HOURS_WORKED_HISTORY, data);
            
            // If it's not already set, set the value for display.
            if (request.getAttribute(CensusConstants.EMPLOYEE_ATTRIBUTE) == null) {
                Employee employee = serviceFacade.getEmployee(Long.parseLong(form
                        .getProfileId()), userProfile, new Date(), true);
                // Must set the employee back for the UI to show the vesting section correctly.
                final Employee employeeForUI = getEmployeeForUI(employee);     
                request.setAttribute(CensusConstants.EMPLOYEE_ATTRIBUTE, employeeForUI);
            }
        }
        ////TODO saveToken(request);
        //TODO
       // return mapping.getInputForward();
        return forwards.get("input");
    }

    /**
     * Save but stop at warning.
     *
     * @param actionForm
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     * @throws SystemExceptio
     */
   @RequestMapping(value ="/editEmployeeSnapshot/",params={"action=save"},  method =  {RequestMethod.POST}) 
   	public String doSave(@Valid @ModelAttribute("editEmployeeForm") EditEmployeeSnapshotForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
   		   	throws IOException,ServletException, SystemException {
	   if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("error");
			}
		}
        String forward =  doSave( actionForm, request, response, false);
        return forward;
    }
    

    @RequestMapping(value ="/editEmployeeSnapshot/",params={"action=showPreviousValue"},  method ={RequestMethod.POST,RequestMethod.GET})
    public String doShowPreviousValue(
    		@Valid @ModelAttribute("editEmployeeForm") EditEmployeeSnapshotForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    	   		   	throws IOException,ServletException, SystemException {
    	if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("error");
			}
		}
    	String forward = doShowPreviousValue( actionForm, request, response);
        return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
    }  
    
    
    /**
     * Save ignore any warning
     *
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     * @throws SystemException
     */
    @RequestMapping(value ="/editEmployeeSnapshot/",params={"action=saveIgnoreWarning"},  method =  {RequestMethod.POST})
    public String doSaveIgnoreWarning( @Valid @ModelAttribute("editEmployeeForm") EditEmployeeSnapshotForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
   		   	throws IOException,ServletException, SystemException {
    	if (bindingResult.hasErrors()) {
	String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	if (errDirect != null) {
		request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
		return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("error");
	}
}
        return doSave( actionForm, request, response, true);
    }

    /**
     * Save the employee, if ignoreWarning == false, then the save will not be done if there are
     * warning from validation. Otherwise it will save with warnings only. It will stop saving if
     * ther are validation error.
     *
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @param ignoreWarning
     * @return
     * @throws IOException
     * @throws ServletException
     * @throws SystemException
     */
    protected String doSave( AutoForm actionForm,
            HttpServletRequest request, HttpServletResponse response, boolean ignoreWarning)
            throws IOException, ServletException, SystemException {

		/*if (!isTokenValid(request)) {
			logger.error("Detect double submission");
			return mapping
					.findForward(Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT);
		}*/
		
		////TODO resetToken(request);

        EditEmployeeSnapshotForm f = (EditEmployeeSnapshotForm) actionForm;

        Employee employee = null;
        Employee oldEmployee=null;
        try {
            UserProfile userProfile = getUserProfile(request);
            Contract currentContract = userProfile.getCurrentContract();
            // if this is an new employee
            boolean ignorePYOS = true;
			boolean ignoreFullyVested = true;
            if (StringUtils.isEmpty(f.getProfileId())) {
            	
                employee = new Employee();
                employee.setEmployeeDetailVO(new EmployeeDetailVO());
                employee.getEmployeeDetailVO().setContractId(userProfile.getCurrentContract().getContractNumber());
            
            } else {
                employee = serviceFacade.getEmployee(Long.parseLong(f
										.getProfileId()), userProfile, new Date(), true);
                oldEmployee=copyEmployeeDetails(employee);
			}

			if (f.isShowVesting()) {
				ignoreFullyVested = false;
			}
			if (f.isHosBasedVesting()) {
				ignorePYOS = false;
            }
			
            // form validation first
            List<ValidationError> errors = new ArrayList<ValidationError>();
            f.validate(errors);
            
            //validate hire date and eligibility date if EC is on.
            List<ValidationError> ecValidationErrors = new ArrayList<ValidationError>();
            if(!ignoreWarning) {
            handleECValidation(f,employee,ecValidationErrors,request);
            }
            
            List<ValidationError> ltptValidationErrors = new ArrayList<ValidationError>();
            if (f.isDisplayLongTermPartTimeAssessmentYearField()) {
				handleLongTermPartTimeAssessmentValidation(f, request, ltptValidationErrors);
			}

            if (errors.size() > 0) {
                f.updateEmployee(employee, true);
                EmployeeValidationErrors validationErrors = serviceFacade.validate(employee,
                        getUserProfile(request), false, ignorePYOS, ignoreFullyVested);
                if (validationErrors.size() > 0) {
                    EmployeeSnapshotErrorUtil.getInstance().convertSkipUnchangedFields(
                            validationErrors, errors, f.getChangedFromInitialValueFieldNames());
                    EmployeeValidationError theError = EmployeeSnapshotErrorUtil
        			.getInstance().getEmployeeValidationError(
        					validationErrors,
        					Property.EMPLOYER_PROVIDED_EMAIL,
        					EmployeeValidationErrorCode.DuplicateEmailAddress);
                    if( theError != null ){
                		EmployeeData employeedata = theError.getEmployees().get(0);
                		request.getSession(false).setAttribute(CensusConstants.EMPLOYEE_DUPLICATE_DATA,employeedata);
                	}
                }
                return handleFormValidationError( f, request, employee, errors);
                
            } else if(!ignoreWarning && ecValidationErrors.size() > 0){            	
            	
					return forwardToInput( request, f, State.Error);				
            	
            } else if(ltptValidationErrors.size() > 0){            	
            	
					return forwardToInput( request, f, State.Error);				
            	
            } else {
                // no form validation error, proceed with updat e
                f.updateEmployee(employee, false);
                checkIfEligibilityOrPlanDataChanged(oldEmployee,employee,f,userProfile.getCurrentContract().getContractNumber(),currentContract);
                //CL -118206-Changes -Begin
    			if(PageErrorType.duplicateEmail.equals(f.getSpecialPageType())) {
    				EmployeeValidationErrors validationErrors = serviceFacade.validate(employee,
    								getUserProfile(request), false, ignorePYOS, ignoreFullyVested);
    				int count = removeErrors(validationErrors);
	                if(count == 1 && validationErrors.contains(EmployeeData.Property.EMPLOYER_PROVIDED_EMAIL,
	                		EmployeeValidationErrorCode.DuplicateEmailAddress,ErrorType.error)){
	                	userProfile = SessionHelper.getUserProfile(request);
	    				EmployeeData employeeData = (EmployeeData)request.getSession(false).getAttribute(CensusConstants.EMPLOYEE_DUPLICATE_DATA);
	    				if (employeeData != null && employeeData.getEmployerProvidedEmail() != null && 
	    						employeeData.getEmployerProvidedEmail().toLowerCase()
	    								.equalsIgnoreCase(f.getValue(EmployeeSnapshotProperties.EmailAddress))) {
	    					
	    					String userId = String.valueOf(userProfile
	    							.getAbstractPrincipal().getProfileId());
	    					String userTypeCode = userProfile.isInternalUser() ? UserIdType.UP_INTERNAL
	    									: UserIdType.UP_EXTERNAL;
	    					String confirmedIndicator = employeeData.isConfirmed() == true ? "Y":"N";
	    					String updateEmployerProvidedEmailAddress = null;
	    					serviceFacade.
	    							updateEmployerProvidedEmailAddress(employeeData.getProfileId(), employeeData.getContractId(), 
	    									updateEmployerProvidedEmailAddress, employeeData.getSourceChannelCode(), confirmedIndicator, userId, userTypeCode);
	    				}
	                	
	                }else if(!(validationErrors.contains(EmployeeData.Property.EMPLOYER_PROVIDED_EMAIL,
	                		EmployeeValidationErrorCode.DuplicateEmailAddress,ErrorType.error))){
	                	f.setSpecialPageType(null);
	                }
    			}
    			//CL -118206-Changes -End
                return updateEmployeeAndForwardToView( f, request,
						employee, ignoreWarning, ignorePYOS, ignoreFullyVested);

            }
         }catch (SystemException e) {
            logger.error("Fail to update employee", e);
            removeFormUponExit( request);
            throw e;
        } catch (EmployeeValidationException e) {
        	EmployeeValidationError theError = EmployeeSnapshotErrorUtil
			.getInstance()
			.getEmployeeValidationError(
					e.getErrors(),
					Property.EMPLOYER_PROVIDED_EMAIL,
					EmployeeValidationErrorCode.DuplicateEmailAddress);
        	if( theError != null ){
        		EmployeeData employeedata = theError.getEmployees().get(0);
        		request.getSession(false).setAttribute(CensusConstants.EMPLOYEE_DUPLICATE_DATA,employeedata);
        	}
        	return handleValidationError( f, request, e, employee);
        }
    }
    /**
     * This method retains a copy of  values from Employee object that are required to invoke eligibility calculation,
     * before it gets updated.
     * @param employee
     * @return
     */
    private Employee copyEmployeeDetails(Employee employee){
    	
    	Employee oldEmployee=new Employee();
    	//populate values into EmployeeDetailVo
    	EmployeeDetailVO employeeDetailVo=new EmployeeDetailVO();
    	employeeDetailVo.setHireDate(employee.getEmployeeDetailVO().getHireDate());
    	employeeDetailVo.setBirthDate(employee.getEmployeeDetailVO().getBirthDate());
    	employeeDetailVo.setEmploymentStatusEffDate(employee.getEmployeeDetailVO().getEmploymentStatusEffDate());
    	employeeDetailVo.setEmploymentStatusCode(employee.getEmployeeDetailVO().getEmploymentStatusCode());
    	//populate values into EmployeeVestingVo
    	EmployeeVestingVO employeeVestingVO=new EmployeeVestingVO();
    	employeeVestingVO.setPlanEligibleInd(employee.getEmployeeVestingVO().getPlanEligibleInd());
    	employeeVestingVO.setYearToDateHrsWorked(employee.getEmployeeVestingVO().getYearToDateHrsWorked());
       	employeeVestingVO.setYearToDateCompEffDt(employee.getEmployeeVestingVO().getYearToDateCompEffDt());
       	oldEmployee.setEmployeeDetailVO(employeeDetailVo);
       	oldEmployee.setEmployeeVestingVO(employeeVestingVO);
       	return oldEmployee;
    }
	/**
	 * This method checks if any of the Plan data or eligibility data or
	 * eligibility information reset data has changed.
	 * 
	 * @param oldEmployee
	 * @param newEmployee
	 * @param form
	 * @param contractNumber
	 * @throws SystemException
	 */
	private void checkIfEligibilityOrPlanDataChanged(Employee oldEmployee,
			Employee newEmployee, EditEmployeeSnapshotForm form,
			int contractNumber,Contract currentContract) throws SystemException {

		if (form.isEligibilityCalcCsfOn()) {

			// ESP-454 check if user has entered eligibility date.
			checkEligibilityDate(newEmployee, form, contractNumber);

			if (form.getProfileId() != null) {
				String employmentStatusWithGreatestEffDate = "";
				try {
					employmentStatusWithGreatestEffDate = getEmploymentStatusWithGreatestEffecDate(
							Long.parseLong(form.getProfileId()), contractNumber, form);
				} catch (ParseException exception) {
					throw new SystemException(
							exception,
							"Error thrown while getting employmentStatusWithGreatestEffDate checkIfEligibilityOrPlanDataChanged method ");
				}

				Date oldHireDate = oldEmployee.getEmployeeDetailVO()
						.getHireDate();
				Date oldDateOfBirth = oldEmployee.getEmployeeDetailVO()
						.getBirthDate();

				// ESP 473-check if hire date is changed
				if (valueUpdated(oldHireDate, newEmployee.getEmployeeDetailVO()
						.getHireDate())) {
					newEmployee.setEligibilityDataChanged(true);
				}

				// ESP 472-check if birth date is changed
				if (valueUpdated(oldDateOfBirth, newEmployee
						.getEmployeeDetailVO().getBirthDate())) {
					newEmployee.setEligibilityDataChanged(true);
				}
				
				// check if the EmploymentStatus corresponding to greatest
				// employment status effective date is equal to Active
				// ESP-474
				if (valueUpdated(oldEmployee.getEmployeeDetailVO()
						.getEmploymentStatusCode(), newEmployee
						.getEmployeeDetailVO().getEmploymentStatusCode())) {
					if (CensusConstants.EMPLOYMENT_STATUS_ACTIVE
							.equals(employmentStatusWithGreatestEffDate)) {
						if (form.isEligibilityCalcCsfOn()) {
							newEmployee.setEligibilityDataChanged(true);
						}
					} else {
						// ESP-475
						checkEmploymentStatus(
								employmentStatusWithGreatestEffDate,
								newEmployee, form);
					}
				}
				
				// ESP-480 Check if employment status effective date field is
				// changed.
				if (valueUpdated(oldEmployee.getEmployeeDetailVO()
						.getEmploymentStatusEffDate(), newEmployee
						.getEmployeeDetailVO().getEmploymentStatusEffDate())) {
					checkEmploymentStatus(employmentStatusWithGreatestEffDate,
							newEmployee, form);
				}
				
				// ESP-476 Check if Plan YTD hours worked field has changed.
				if ((newEmployee.getEmployeeVestingVO()
						.getYearToDateHrsWorked() != null)
						&& (!(newEmployee.getEmployeeVestingVO()
								.getYearToDateHrsWorked().equals(oldEmployee
								.getEmployeeVestingVO()
								.getYearToDateHrsWorked())))) {
					checkEligibilityDateIsNullOrGreaterThanYTD(newEmployee,
							form);
				}
				
				// ESP-477 check if Plan YTD hours worked /eligible comp
				// effective date field is
				// changed
				if (valueUpdated(newEmployee.getEmployeeVestingVO()
						.getYearToDateCompEffDt(), oldEmployee
						.getEmployeeVestingVO().getYearToDateCompEffDt())) {
					checkEligibilityDateIsNullOrGreaterThanYTD(newEmployee,
							form);
				}
				
				// ESP-442/ESP-443 Check Eligible to participate Indicator
				if ((!StringUtils.isEmpty(newEmployee.getEmployeeVestingVO()
						.getPlanEligibleInd()))
						&& (!(newEmployee.getEmployeeVestingVO()
								.getPlanEligibleInd()).equals(oldEmployee
								.getEmployeeVestingVO().getPlanEligibleInd()))) {
					checkEligibleToParticipateField(newEmployee, form);
				}

				// Check Long term part time assessment field
				checkLongTermPartTimeAssessmentField(newEmployee, form, contractNumber,currentContract);
				
				// ESP-460 check if user unchecks Calculation override
				// indicator.
				checkCalculationOverrideField(newEmployee, form);
			}
		} else {
			
			// ESP.455 If the CSF Eligibility calculation service is not equal to
			// Yes and the CSF EZStart service is equal to Yes and this page
			// element has been
			// entered in Add mode or changed in Edit Mode
			// and the Save page element has been successfully selected (no errors
			// or warnings) then
			// Invoke Plan Entry Calculation
			try {
				ContractServiceFeature ezStartCSF = null;
				ContractServiceDelegate delegate = ContractServiceDelegate
						.getInstance();
				ezStartCSF = delegate.getContractServiceFeature(contractNumber,
						ServiceFeatureConstants.AUTO_ENROLLMENT_FEATURE);

				if (ServiceFeatureConstants.YES.equalsIgnoreCase(ezStartCSF
						.getValue())) {
					if (StringUtils.isNotEmpty((String) form
							.getValue("eligibilityDate"))) {
						Date eligibilityDate = eligibilityDateFormat
								.parse((String) form
										.getValue("eligibilityDate"));
						if (valueUpdated(null, eligibilityDate)) {
							// Set moneyType for Plan entry date calculation
							newEmployee.setPlanDataChanged(true);
						}
					}
				}
			} catch (ApplicationException exception) {
				throw new SystemException(
						exception,
						"Error thrown while calling EZStart csf in checkIfEligibilityOrPlanDataChanged method ");
			} catch (ParseException exception) {
				throw new SystemException(
						exception,
						"Error thrown while calling EZStart csf in checkIfEligibilityOrPlanDataChanged method ");
			}
		}
	}
	
	/**
	 * Check if Long Term Part Time Assessment Year is changed
	 * 
	 * @param newEmployee
	 * @param form
	 * @param contractNumber
	 * @param request
	 * @throws SystemException
	 */
	private void checkLongTermPartTimeAssessmentField(Employee newEmployee, EditEmployeeSnapshotForm form,
			int contractNumber,Contract currentContract) throws SystemException {

		if (form.isEligibilityCalcCsfOn() && !(Constants.PRODUCT_RA457.equalsIgnoreCase(currentContract.getProductId()))
				&& form.isDisplayLongTermPartTimeAssessmentYearField()) {

			// Get latest Long Term Part Time Assessment Year
			int longTermPartTimeAssessmentYear = LongTermPartTimeAssessmentUtil.getInstance()
					.evaluateLongTermPartTimeAssessmentYear(contractNumber, Integer.parseInt(form.getProfileId()),
							null);

			// check if long term part time assessment year is changed
			if (longTermPartTimeAssessmentYear != form.getLongTermPartTimeAssessmentYear()) {

				List<EmployeePlanEntryVO> nonEligiblePartTimeEmployeePlanEntryVoList = LongTermPartTimeAssessmentUtil
						.getInstance().getNonEligiblePartTimeEmployeePlanEntryList(contractNumber,
								Integer.parseInt(form.getProfileId()), null);

				if (!nonEligiblePartTimeEmployeePlanEntryVoList.isEmpty()) {
					
					Date previousPartTimeComputationPeriodEndDate = LongTermPartTimeAssessmentUtil.getInstance()
							.getCurrentPlanYearEndDate(contractNumber);

					for (EmployeePlanEntryVO vo : nonEligiblePartTimeEmployeePlanEntryVoList) {

						// set p/t override values
						vo.setPartTimeQualificationYear(form.getLongTermPartTimeAssessmentYear());
						vo.setPreviousPartTimeQualificationYear(
								new BigDecimal(PartTimeQulificationYear.YEAR_PT_OVERRIDE.getQulificationYear()));
						vo.setPreviousPartTimeComputationPeriodEndDate(previousPartTimeComputationPeriodEndDate);

						serviceFacade.createOrUpdateLongTermPartTimeAssessmentEmployeePlanEntryDetail(vo,
								newEmployee.getUserId(), newEmployee.getUserIdType(), Constants.PS_SOURCE_CHANNEL_CODE,
								Constants.PS_SOURCE_FUNCTION_CODE_EES);

					}
					
					newEmployee.setEligibilityDataChanged(true);
				}
			}
		}
	}

	/**
	 * This method checks if Eligible to participate element has been changed and EC csf is on for a money type
	 * but the corresponding eligibility date is not available.
	 * 
	 * @param newEmployee
	 * @param form
	 */
	private void checkEligibleToParticipateField(Employee newEmployee,
			EditEmployeeSnapshotForm form) {
		
		if (form.isEligibilityCalcCsfOn()) {
			
			ArrayList<EligibilityCalculationMoneyType> eligibilityServiceMoneyTypeList = form
					.getEligibilityServiceMoneyTypes();
			Iterator<EligibilityCalculationMoneyType> moneyTypeListIterator = eligibilityServiceMoneyTypeList
					.iterator();

			while (moneyTypeListIterator.hasNext()) {
				EligibilityCalculationMoneyType moneyType = (EligibilityCalculationMoneyType) moneyTypeListIterator.next();
				
				// ESP 442.
				if (Constants.YES.equals(newEmployee.getEmployeeVestingVO()
						.getPlanEligibleInd())) {
					if (StringUtils.isEmpty(moneyType.getEligibilityDate())) {
						newEmployee.setEligibilityDataChanged(true);
					}// ESP 443.
				} else if (Constants.NO.equals(newEmployee
						.getEmployeeVestingVO().getPlanEligibleInd())) {
					if ((!StringUtils.isEmpty(moneyType.getEligibilityDate()))
							&& (!(Constants.YES).equals(moneyType
									.getCalculationOverride()))) {
						newEmployee.setEligibilityResetDataChanged(true);
						newEmployee.getEligbilityResetDataMoneyTypes().add(
								moneyType.getMoneyTypeId());
					}
				}
			}
		}
	} 
	
	/**
	 * This method checks if user has entered eligibility date or not
	 * 
	 * @param newEmployee
	 * @param form
	 * @param contractNumber
	 * @throws SystemException
	 */
	private void checkEligibilityDate(Employee newEmployee,
			EditEmployeeSnapshotForm form, int contractNumber)
			throws SystemException {

		EligibilityServiceDelegate eligibilityServiceDelegate = EligibilityServiceDelegate
				.getInstance(Constants.PS_APPLICATION_ID);
		ArrayList<EligibilityCalculationMoneyType> eligibilityServiceMoneyTypeList = form
				.getEligibilityServiceMoneyTypes();
		Iterator<EligibilityCalculationMoneyType> moneyTypeListIterator = eligibilityServiceMoneyTypeList
				.iterator();

		while (moneyTypeListIterator.hasNext()) {
			EligibilityCalculationMoneyType moneyType = (EligibilityCalculationMoneyType) moneyTypeListIterator
					.next();
			EmployeePlanEntryVO employeePlanEntryVo = null;
			
			if (form.getProfileId() != null) {
				employeePlanEntryVo = eligibilityServiceDelegate
						.getEmployeePlanEntryDetails(contractNumber, Integer
								.parseInt(form.getProfileId()), moneyType
								.getMoneyTypeId());
			}
			
			Date newEligibilityDate = null;
			if (StringUtils.isNotEmpty(moneyType.getEligibilityDate())) {
				newEligibilityDate = new Date(moneyType.getEligibilityDate());
			}
			
			if (employeePlanEntryVo != null) {
				if (newEligibilityDate == null) {
					newEmployee.setEligibilityDataChanged(true);
				} else {
					if (valueUpdated(employeePlanEntryVo.getEligibilityDate(),
							newEligibilityDate)) {
						// Set moneyType for Plan entry date calculation
						newEmployee.setPlanDataChanged(true);
					}
				}
			} else {
				if (valueUpdated(null, newEligibilityDate)) {
					// Set moneyType for Plan entry date calculation
					newEmployee.setPlanDataChanged(true);
				}
			}
		}
	}
    
    /**
	 * If Calculation override field is unchecked invoke Eligibility
	 * calculation.
	 * 
	 * @param newEmployee
	 * @param form
	 * @param contractNumber
	 * @throws SystemException
	 */
	private void checkCalculationOverrideField(Employee newEmployee,
			EditEmployeeSnapshotForm form) throws SystemException {
	
		for (int i = 0; i < form.getEligibilityServiceMoneyTypes().size(); i++) {

			EligibilityCalculationMoneyType currentECalcMoneyType = form
					.getEligibilityServiceMoneyTypes().get(i);
			EligibilityCalculationMoneyType lastUpdatedECalcMoneyType = form
					.getLastUpdatedEligibilityServiceMoneyTypes().get(i);

			String oldCalculationOverride = lastUpdatedECalcMoneyType
					.getCalculationOverride();
			String newCalculationOverride = currentECalcMoneyType
					.getCalculationOverride();
			
			if ((!StringUtils.equalsIgnoreCase(oldCalculationOverride, newCalculationOverride))
					&& (Constants.YES).equalsIgnoreCase(oldCalculationOverride)
					&& !(Constants.YES).equalsIgnoreCase(newCalculationOverride)) {
				newEmployee.setEligibilityDataChanged(true);
			}
		}
	}
	
	/**
	 * This method checks if eligibility date is greater than eligible comp
	 * effective date.
	 * 
	 * @param oldEmployee
	 * @param newEmployee
	 * @param moneyTypeList
	 * @param form
	 */
	private void checkEligibilityDateIsNullOrGreaterThanYTD(
			Employee newEmployee, EditEmployeeSnapshotForm form) {
		
		Date yearToDateCompEffDt = newEmployee.getEmployeeVestingVO()
				.getYearToDateCompEffDt();
		if (form.isEligibilityCalcCsfOn()) {
			ArrayList<EligibilityCalculationMoneyType> eligibilityServiceMoneyTypeList = form
					.getEligibilityServiceMoneyTypes();
			Iterator<EligibilityCalculationMoneyType> moneyTypeListIterator = eligibilityServiceMoneyTypeList
					.iterator();

			while (moneyTypeListIterator.hasNext()) {
				EligibilityCalculationMoneyType moneyType = (EligibilityCalculationMoneyType) moneyTypeListIterator
						.next();
				if (StringUtils.isEmpty(moneyType.getEligibilityDate())) {
					newEmployee.setEligibilityDataChanged(true);
				} else if (StringUtils.isNotEmpty(moneyType.getEligibilityDate())
						&& (yearToDateCompEffDt != null)
						&& (new Date(moneyType.getEligibilityDate())
								.compareTo(yearToDateCompEffDt) >= 0)) {
					newEmployee.setEligibilityDataChanged(true);
				}

			}
		}
	}
	
	/**ESP 475 & ESP 480
	 *  This method checks if the employment status is Terminate or Retired or Deceased or Cancelled
	 *  for the greatest status effective date and if the Ec service is yes for a particular money type and if the
	 *  corresponding override ind is unchecked and if the eligibility date is greater than or equal to 
	 *  employment status effective date.
	 * @param employmentStatusWithGreatestEffDate
	 * @param oldEmployee
	 * @param newEmployee
	 * @param moneyTypeList
	 * @param form
	 */
	private void checkEmploymentStatus(
			String employmentStatusWithGreatestEffDate, 
			Employee newEmployee,EditEmployeeSnapshotForm form) {
		if (employmentStatusWithGreatestEffDate.equalsIgnoreCase(CensusConstants.EMPLOYMENT_STATUS_DECEASED)
				|| employmentStatusWithGreatestEffDate.equalsIgnoreCase(CensusConstants.EMPLOYMENT_STATUS_CANCEL)
				|| employmentStatusWithGreatestEffDate.equalsIgnoreCase(CensusConstants.EMPLOYMENT_STATUS_TERMINATED)
				|| employmentStatusWithGreatestEffDate.equalsIgnoreCase(CensusConstants.EMPLOYMENT_STATUS_RETIRED)
				||employmentStatusWithGreatestEffDate.equalsIgnoreCase(CensusConstants.EMPLOYMENT_STATUS_DISABLED)) {
			if (form.isEligibilityCalcCsfOn()) {
			
			int totalNumberOfMoneyTypes = form.getEligibilityServiceMoneyTypes()
				.size();

		    for (int i = 0; i < totalNumberOfMoneyTypes; i++) {

			EligibilityCalculationMoneyType currentECalcMoneyType = form
					.getEligibilityServiceMoneyTypes().get(i);
			EligibilityCalculationMoneyType lastUpdatedECalcMoneyType = form
					.getLastUpdatedEligibilityServiceMoneyTypes().get(i);
			String oldCalculationOverride = lastUpdatedECalcMoneyType
					.getCalculationOverride();
			String newCalculationOverride = currentECalcMoneyType
					.getCalculationOverride();
			if (!(Constants.YES).equalsIgnoreCase(newCalculationOverride)) {
						if (!StringUtils.isEmpty(currentECalcMoneyType
								.getEligibilityDate())&& newEmployee.getEmployeeDetailVO().getEmploymentStatusEffDate()!=null) {
							if (new Date(currentECalcMoneyType.getEligibilityDate()).compareTo(newEmployee.getEmployeeDetailVO().getEmploymentStatusEffDate())>=0) {
								newEmployee.setEligibilityResetDataChanged(true);
								newEmployee.getEligbilityResetDataMoneyTypes()
										.add(currentECalcMoneyType.getMoneyTypeId());
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * This method finds the employment status with greatest status effective date. 
	 * @param profileId
	 * @param contractNumber
	 * @param form
	 * @return
	 * @throws SystemException
	 * @throws ParseException
	 */
    private String getEmploymentStatusWithGreatestEffecDate(Long profileId,
			int contractNumber,EditEmployeeSnapshotForm form) throws SystemException,ParseException {
		String employmentStatusWithGreatestEffDate =form.getValue(EmployeeSnapshotProperties.EmploymentStatus);
		// get employment status history
		
		// set as of date as '9999-12-31'
		Calendar asOfDate = Calendar.getInstance();
		asOfDate.set(9999,11,31);

		EmployeeVestingInfo statusParamInfo = serviceFacade
				.getEmployeeVestingInfo(profileId,contractNumber, asOfDate.getTime(),
						VestingType.EMPLOYMENT_STATUS, false, true, false);
		String greatestStatusEffectiveDateString =form.getValue(EmployeeSnapshotProperties.EmploymentStatusEffectiveDate);
		Date  greatestStatusEffectiveDate=null;
		DateFormat df = new SimpleDateFormat(RenderConstants.MEDIUM_MDY_SLASHED);
		if(!StringUtils.isEmpty(greatestStatusEffectiveDateString)){
			greatestStatusEffectiveDate=df.parse(greatestStatusEffectiveDateString);
		}
		EmployeeStatusInfo employeeStatusInfo = statusParamInfo.getCurrentStatus();
		//Iterator statusListIterator = statusList.iterator();

		//while (statusListIterator.hasNext()) {
		//	EmployeeStatusInfo employeeStatusInfo = (EmployeeStatusInfo) statusListIterator
		//			.next();
		Date statusEffectiveDate = null;
		if (employeeStatusInfo != null) {
			statusEffectiveDate = employeeStatusInfo.getEffectiveDate();
		}
		if (statusEffectiveDate != null) {
			if (statusEffectiveDate.after(greatestStatusEffectiveDate)) {
				greatestStatusEffectiveDate = statusEffectiveDate;
				employmentStatusWithGreatestEffDate = employeeStatusInfo.getStatus();
			}
		}
		//	}
	
		return employmentStatusWithGreatestEffDate;
	}
    
    /**
     * This method can check if the date value has been updated 
     * @param value1
     * @param value2
     * @return
     */
    private boolean valueUpdated(Date value1, Date value2) {
		if (value1 == null) {
			return (value2 !=null);
		} else {
			if (value2==null){
				return true;
			}else{
			return (value1.compareTo(value2))!=0;
			}
		}
	}
    
    /**
     * This method compares if the calculation override indicator is updated or not
     * @param value1
     * @param value2
     * @return
     */
    private static boolean calculationOverrideUpdated(String value1,String value2){
    	if (value1 == null || value1 .equals(Constants.NO)) {
			return (value2 !=null && !value2.equals(Constants.NO));
		} else {
			if (value2==null || value2.equals(Constants.NO)){
				if(value1.equals(Constants.YES)){
				return true;
				}else{
				return false;
				}
			}else{
			return !(value1.equals(value2));
			}
		}
    }
    /**
     * 
     * @param value1
     * @param value2
     * @return
     */
    private boolean valueUpdated(String value1, String value2) {
		if (value1 == null) {
			return (value2 !=null);
		} else {
			if (value2==null) return true;
			return !value1.equals(value2); 
		}
	} 
    /**
     * Send employee update to service, upon success, forward to view page
     *
     * @param mapping
     * @param form
     * @param request
     * @param employee
     * @param ignoreWarning Ignore warnings when update employee if it is true
     * @return
     * @throws IOException
     * @throws ServletException
     * @throws SystemException
     * @throws EmployeeValidationException
     */
    protected String updateEmployeeAndForwardToView(
            EditEmployeeSnapshotForm form, HttpServletRequest request, Employee employee,
            boolean ignoreWarning,
            boolean ignorePYOS, boolean ignoreFullyVested) throws IOException, ServletException, SystemException,
            EmployeeValidationException {
    	updateEligbleServiceAttributes(form,employee,request);
        Employee updatedEmployee = serviceFacade.updateEmployee(employee,
						getUserProfile(request), ignoreWarning, form
						.doNotCheckSimilarSsn(), ignorePYOS, ignoreFullyVested);
        
        // for new employee, it needs to set the profile id
        form.setProfileId(updatedEmployee.getEmployeeDetailVO().getProfileId().toString());
        return forwardToViewEmployeeSnapshot( request, form);
    }

	private void updateEligbleServiceAttributes(EditEmployeeSnapshotForm form,
			Employee employee, HttpServletRequest request)
			throws SystemException {
		
		// Check if user has edited Eligibility Date or Calculation override
		// field for any money type.
		ArrayList employeePlanEntryVoList = new ArrayList();
		ArrayList eligibleEmployeePlanEntryVoList = new ArrayList();
		int contractId = getUserProfile(request).getCurrentContract()
				.getContractNumber();
		
		try {
			if (form.isEligibilityCalcCsfOn()
					&& form.getEligibilityServiceMoneyTypes() != null
					&& form.getEligibilityServiceMoneyTypes().size() > 0) {

				int totalNumberOfMoneyTypes = form
						.getEligibilityServiceMoneyTypes().size();
				for (int i = 0; i < totalNumberOfMoneyTypes; i++) {
					EligibilityCalculationMoneyType currentECalcMoneyType = form
							.getEligibilityServiceMoneyTypes().get(i);
					EligibilityCalculationMoneyType lastUpdatedECalcMoneyType = form
							.getLastUpdatedEligibilityServiceMoneyTypes()
							.get(i);
					employee.setEligibleEmployeePlanEntryVoList(eligibleEmployeePlanEntryVoList);
					employee.setEmployeePlanEntryVoList(employeePlanEntryVoList);

					Date oldEligibilityDate = null;
					Date newEligibilityDate = null;
					String oldCalculationOverride = lastUpdatedECalcMoneyType
							.getCalculationOverride();
					String newCalculationOverride = currentECalcMoneyType
							.getCalculationOverride();
					
					if (!StringUtils.isEmpty(lastUpdatedECalcMoneyType
							.getEligibilityDate())) {
						oldEligibilityDate = eligibilityDateFormat
								.parse(lastUpdatedECalcMoneyType
										.getEligibilityDate());
					}
					
					if (!StringUtils.isEmpty(currentECalcMoneyType
							.getEligibilityDate())) {
						newEligibilityDate = eligibilityDateFormat
								.parse(currentECalcMoneyType
										.getEligibilityDate());
						if (!employee.isEligibilityDateByMoneyTypePresent()) {
							employee.setEligibilityDateByMoneyTypePresent(true);
						}
					}

					// The eligibility date corresponding to EEDEF money type
					// should be in sync with employee contract eligibility
					// date.
					if (ServiceFeatureConstants.EMPLOYEE_ELECTIVE_DEFERAL
							.equalsIgnoreCase(currentECalcMoneyType
									.getMoneyTypeId())) {
						if ((valueUpdated(oldEligibilityDate,
								newEligibilityDate))) {
							employee.getEmployeeVestingVO().setEligibilityDate(
									newEligibilityDate);
						}
					}
					
					EmployeePlanEntryVO employeePlanEntryVo = getEmployeeplanEntryVo(
							request, employee, form, currentECalcMoneyType
									.getMoneyTypeId());
					
					if ((calculationOverrideUpdated(oldCalculationOverride,
							newCalculationOverride))
							|| (valueUpdated(oldEligibilityDate,
									newEligibilityDate))) {
						// EmployeePlanEntryVO
						// employeePlanEntryVo=getEmployeeplanEntryVo(request,employee,form,currentECalcMoneyType.getMoneyTypeId());
						// ESP 459 if Calculation override field is unchecked
						// then automatically set
						// Eligibility date and Eligible plan entry date to
						// null.
						if (!(Constants.YES)
								.equalsIgnoreCase(newCalculationOverride)
								&& ((Constants.YES)
										.equalsIgnoreCase(oldCalculationOverride))) {
							employeePlanEntryVo.setEligibilityDate(null);
							employeePlanEntryVo
									.setEligibilityPlanEntryDate(null);
							employee
									.setEligibilityDateByMoneyTypePresent(false);
							newCalculationOverride = Constants.NO;
						} else if ((valueUpdated(oldEligibilityDate,
								newEligibilityDate))) {
							employeePlanEntryVo
									.setEligibilityDate(newEligibilityDate);
						}
						if (newCalculationOverride == null) {
							employeePlanEntryVo
									.setEligibilityProvidedIndicator(Constants.NO);
						} else {
							employeePlanEntryVo
									.setEligibilityProvidedIndicator(newCalculationOverride);
						}
						employeePlanEntryVoList.add(employeePlanEntryVo);

					}
					
					// this list is populated for money type combined edit
					// validations
					eligibleEmployeePlanEntryVoList.add(employeePlanEntryVo);
				}
			}// ESP-453
			else {
				if (form.isEligibilityDateValueChanged()
						&& checkIfContractHasEEDEFMoneyType(contractId)) {
					EmployeePlanEntryVO employeePlanEntryVo = getEmployeeplanEntryVo(
							request, employee, form,
							ServiceFeatureConstants.EMPLOYEE_ELECTIVE_DEFERAL);
					if (StringUtils.isNotEmpty((String) form
							.getValue("eligibilityDate"))) {
						Date eligibilityDate = eligibilityDateFormat
								.parse((String) form
										.getValue("eligibilityDate"));
						employeePlanEntryVo.setEligibilityDate(eligibilityDate);
						employeePlanEntryVo
								.setEligibilityProvidedIndicator(ServiceFeatureConstants.YES);
					} else { // ESP-479
						employeePlanEntryVo.setEligibilityDate(null);
						employeePlanEntryVo
								.setEligibilityProvidedIndicator(ServiceFeatureConstants.NO);
					}
					
					employeePlanEntryVoList.add(employeePlanEntryVo);
					employee
							.setEmployeePlanEntryVoList(employeePlanEntryVoList);
				}
			}
			
			// (EC 2)CR14 - Plan Indicator Update
			this.setPlanEligibleIndicator(employee, contractId);
			

		} catch (ParseException e) {
			logger.error(
					"Error occured while parsing eligibilityDate: eligibilityDate::"
							+ form.getValue("eligibilityDate"), e);
		}
	}
	
	/**
	 * This method checks if the contract has EEDEF money type
	 * @param contractId
	 * @return
	 * @throws SystemException
	 */
	private boolean checkIfContractHasEEDEFMoneyType(int contractId)throws SystemException{
		boolean hasEEDEF=false;
		ContractServiceDelegate	delegate = ContractServiceDelegate.getInstance();
		List<MoneyTypeEligibilityCriterion> moneyTypeList=delegate.getEligibleMoneyTypes(contractId);
		Iterator<MoneyTypeEligibilityCriterion> moneyTypeListIterator=moneyTypeList.iterator();
		while(moneyTypeListIterator.hasNext()){
			MoneyTypeEligibilityCriterion moneyType=(MoneyTypeEligibilityCriterion)moneyTypeListIterator.next();
		if(ServiceFeatureConstants.EMPLOYEE_ELECTIVE_DEFERAL.equalsIgnoreCase(moneyType.getMoneyTypeId())){
			hasEEDEF=true;
		}
		}
		return hasEEDEF;
	}
/**
 * This method gets employee plan entry object from employee service if exists else returns a new object.
 * @param request
 * @param employee
 * @param form
 * @param currentECalcMoneyType
 * @return
 * @throws SystemException
 */
	private EmployeePlanEntryVO getEmployeeplanEntryVo(
			HttpServletRequest request, Employee employee,
			EditEmployeeSnapshotForm form, String currentECalcMoneyType)
			throws SystemException {

		UserProfile userProfile = getUserProfile(request);
		String userIdType = "";
		if (userProfile.isInternalUser()) {
			userIdType = Constants.INTERNAL_USER_ID_TYPE;
		} else {
			userIdType = Constants.EXTERNAL_USER_ID_TYPE;
		}
		
		EligibilityServiceDelegate eligibilityServiceDelegate = EligibilityServiceDelegate
		.getInstance(Constants.PS_APPLICATION_ID);
		EmployeePlanEntryVO employeePlanEntryVo =null;
		if(form.getProfileId()!=null){
		 employeePlanEntryVo = eligibilityServiceDelegate
				.getEmployeePlanEntryDetails(employee.getEmployeeDetailVO()
						.getContractId(),
						Integer.parseInt(form.getProfileId()),
						currentECalcMoneyType);
		}
		if (employeePlanEntryVo == null) {
			employeePlanEntryVo = new EmployeePlanEntryVO();
		}
		employeePlanEntryVo.setMoneyTypeId(currentECalcMoneyType);
		employeePlanEntryVo.setContractId(userProfile.getCurrentContract().getContractNumber());
        if(employee.getEmployeeDetailVO()!=null && employee.getEmployeeDetailVO().getProfileId()!=null){
		employeePlanEntryVo.setProfileId(new BigDecimal(employee
				.getEmployeeDetailVO().getProfileId()));
        }
		employeePlanEntryVo.setUserId(String.valueOf(userProfile.getPrincipal().getProfileId()));
		employeePlanEntryVo.setUserIdType(userIdType);
		employeePlanEntryVo.setSourceCode(Constants.PS_APPLICATION_ID);

		return employeePlanEntryVo;
	}
    /**
     * Handle the case for validation error in the form.
     *
     * @param mapping
     * @param form
     * @param request
     * @param errors
     * @return
     */
    protected String handleFormValidationError(
            EditEmployeeSnapshotForm form, HttpServletRequest request, Employee employee,
            List<ValidationError> errors) throws SystemException {
        form.updateEmployee(employee, true);
		addVestingErrors(errors, form.getVestingInformation());
		setErrors(form, request, EmployeeSnapshotErrorUtil.getInstance().sort(
				errors));
        return forwardToInput( request, form, State.Error);
    }

    /**
     * Handle Business validation errors
     *
     * @param mapping
     * @param form
     * @param request
     * @param e
     * @param employee
     * @return
     * @throws IOException
     * @throws ServletException
     * @throws SystemException
     */
    protected String handleValidationError(
            EditEmployeeSnapshotForm form, HttpServletRequest request,
            EmployeeValidationException e, Employee employee) throws IOException, ServletException,
            SystemException {
        List<ValidationError> errors = new ArrayList<ValidationError>();
        EmployeeSnapshotErrorUtil.getInstance().setChangedSet(form.getChangedFromInitialValueFieldNames());
        EmployeeSnapshotErrorUtil.getInstance().convertSkipUnchangedFields(e.getErrors(), errors,
                form.getChangedFromInitialValueFieldNames());
        EmployeeValidationError ssnWarning = EmployeeSnapshotErrorUtil.getInstance()
                .getEmployeeValidationError(e.getErrors(), Property.SOCIAL_SECURITY_NUMBER,
                        EmployeeValidationErrorCode.SimilarSSN);
        if (logger.isDebugEnabled()) {
            logger.debug("Validation Errors: )" + e.getErrors());
        }
        // The validation errors were only non-persist warnings
        if (ssnWarning == null && errors.size() == 0) {
            try {
                return updateEmployeeAndForwardToView( form, request,
						employee, true, !form.isHosBasedVesting(), !form.isShowVesting());
            } catch (EmployeeValidationException ex) {
                // this time, it must be hard Exception
                EmployeeSnapshotErrorUtil.getInstance()
										.convertSkipUnchangedFields(e.getErrors(), errors,
												form.getChangedFieldNames());
				ssnWarning = EmployeeSnapshotErrorUtil.getInstance()
										.getEmployeeValidationError(ex.getErrors(),
								Property.SOCIAL_SECURITY_NUMBER,
                        EmployeeValidationErrorCode.SimilarSSN);
            }

        }

        // Must set the employee back for the UI to show the vesting section correctly.
        final Employee employeeForUI = getEmployeeForUI(employee);     
        request.setAttribute(CensusConstants.EMPLOYEE_ATTRIBUTE, employeeForUI);

        if (ssnWarning == null) {
			addVestingErrors(errors, form.getVestingInformation());
            setErrors(form,request, errors);
            form.expandOnErrors(errors);
            if (EmployeeSnapshotErrorUtil.getInstance().hasWarningsOnly(errors)) {
                return forwardToInput( request, form, State.Warning);
            } else {
                return forwardToInput( request, form, State.Error);
            }
        } else {
            errors.clear();
            errors.add(ssnWarningValidationError);
            setErrorsInRequest(request, errors);
            form.expandOnErrors(errors);
            request.setAttribute(SSNWarningAttribute, ssnWarning);
            request.setAttribute(SSNWarningMessage, ssnWarningValidationError);

            return forwardToInput( request, form, State.SimilarSsnError);
        }
        // the errors are warning only
    }
    
   /**
    * CR#15 When Ec is on hire date cannot be deleted .
    * Warning message is shown if Eligiblity Ind is changed to No when any of the money types has eligbility date
    * @param mapping
    * @param form
    * @param request
    * @param employee
    * @return
    * @throws IOException
    * @throws ServletException
    * @throws SystemException
    */
    protected void handleECValidation(EditEmployeeSnapshotForm form,Employee employee,
            List<ValidationError> errors,HttpServletRequest request)  {
    	Set set=form.getChangedFromInitialValueFieldNames();
    	if(set!=null){
    		//ESP 482
    		if(form.isEligibilityCalcCsfOn()){
    		if(set.contains(EmployeeSnapshotProperties.HireDate)&&StringUtils.isEmpty(form.getValue(EmployeeSnapshotProperties.HireDate))){
    		
    			errors.add(new ValidationError(
    					EmployeeSnapshotProperties.HireDate, CensusErrorCodes.HireDateCannotBeDeleted,
    					Type.error));
    			setErrors(form,request, errors);
    	  		
    		}
			}
		}
	}
    
    
	/**
	 * Long-term Part-time Assessment Year value on the page = either 2 or 3, but
	 * the hire date for employee record is within 12 months previous to the current
	 * date
	 * 
	 * @param form
	 * @param employee
	 * @param errors
	 * @param request
	 */
	protected void handleLongTermPartTimeAssessmentValidation(EditEmployeeSnapshotForm form,
			HttpServletRequest request, List<ValidationError> errors) {

		String hireDateString = form.getValue(EmployeeSnapshotProperties.HireDate);

		if (form.isEligibilityCalcCsfOn() && form
				.getLongTermPartTimeAssessmentYear() > EmployeeEligibilityAssessmentReqConstants.DEFAULT_LONG_TERM_PART_TIME_ASSESSMENT_YEAR) {

			if (StringUtils.isNotBlank(hireDateString)) {
				Date hireDate = null;

				try {
					hireDate = dateFormat.parse(hireDateString);
				} catch (ParseException e) {
					// swallow the exception, Noting to do
					// Employee snapshot's hiredate validations will handle this
				}

				if (hireDate != null) {
					Calendar anniversaryDateCal = Calendar.getInstance();
					anniversaryDateCal.setTime(hireDate);
					anniversaryDateCal.add(Calendar.YEAR, 1);

					Date currentDate = new Date();
					Calendar currentCal = Calendar.getInstance();
					currentCal.setTime(currentDate);
					currentCal.set(Calendar.HOUR, 0);
					currentCal.set(Calendar.MINUTE, 0);
					currentCal.set(Calendar.SECOND, 0);

					if (!anniversaryDateCal.before(currentCal)) {
						errors.add(new ValidationError(EmployeeSnapshotProperties.LTPTAssessmentYear,
								CensusErrorCodes.LongTermPartTimeAssessmentYearOverrideError, Type.error));
						setErrors(form,request, errors);
					}
				}

			} else {

				errors.add(new ValidationError(EmployeeSnapshotProperties.LTPTAssessmentYear,
						CensusErrorCodes.LongTermPartTimeAssessmentYearOverrideError, Type.error));
				setErrors(form,request, errors);

			}
		}
	}
	
    /*
     * When the user comes to edit/add page from other pages, load/create employee and set up the
     * form.
     *
     * @see com.manulife.pension.ps.web.controller.PsAutoAction#doDefault(org.apache.struts.action.ActionMapping,
     *      com.manulife.pension.ps.web.controller.BaseAutoForm,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @RequestMapping(value ="/editEmployeeSnapshot/",  method =  {RequestMethod.GET}) 
   	public String doDefault (@Valid @ModelAttribute("editEmployeeForm") EditEmployeeSnapshotForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
   	throws IOException,ServletException, SystemException {
    	if (bindingResult.hasErrors()) {
    		String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if (errDirect != null) {
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("error");
    		}
    	}
        if (logger.isDebugEnabled())
            logger.debug(EditEmployeeSnapshotController.class.getName()
                    + ":forwarding to Employee Snapshot - Edit.");

        int contractNumber = 0;
        UserProfile userProfile = getUserProfile(request);
        Contract currentContract = userProfile.getCurrentContract();
        UserInfo userInfo = SecurityServiceDelegate.getInstance().getUserInfo(userProfile.getPrincipal());

        EmployeeSnapshotSecurityProfile securityProfile = new EmployeeSnapshotSecurityProfile(
                userProfile);
        
        EmployeeSnapshotErrorUtil.getInstance().setChangedSet(null);
        

        // no update permission
        if (!securityProfile.isUpdateCensusData() || securityProfile.isContractDI()) {
            return redirectForNotAccessible();
        }

        contractNumber = userProfile.getCurrentContract().getContractNumber();

        EditEmployeeSnapshotForm f = (EditEmployeeSnapshotForm) actionForm;
        // initialize the form for expansion etc..
        initializeForm(f);
        if (f.getProfileId() == null) {
            logger.error("No profileId is passed in: ");
           //return mapping.findForward(SYSTEM_ERROR_PAGE);
            return forwards.get(SYSTEM_ERROR_PAGE);
        }
        long profileId = Long.parseLong(f.getProfileId());
        Employee employeeSnapsot = serviceFacade.getEmployee(profileId,
				userProfile, new Date(), f.isShowVesting());
        String employeeSSN = employeeSnapsot.getEmployeeDetailVO().getSocialSecurityNumber();
        boolean isExternalUser = userProfile.getRole() instanceof ExternalUser;
        request.getSession().setAttribute("challengeRequestFrom", "editEmployeeSnapshotPage");
		if (isExternalUser && userInfo.getSsn().equals(employeeSSN) && null != request.getSession(false).getAttribute(Constants.CHALLENGE_PASSCODE_IND)) {
				boolean isChallengedAlready = (Boolean) request.getSession(false).getAttribute(Constants.CHALLENGE_PASSCODE_IND);
			
				if (isChallengedAlready) {
				//request.getSession(false).setAttribute("challengeRequestFrom", "editEmployeeSnapshotPage");
				request.getSession(false).setAttribute("employeeProfileId", profileId);
				return forwards.get("challengePasscode");
			}
		}
        // EmployeeInformation employee = null;
        try {
        	
        	setVestingSection(contractNumber, f);
			// initialize the form for expansion etc..
			initializeSectionExpansion(f);

			Employee employee = serviceFacade.getEmployee(profileId,
								userProfile, new Date(), f.isShowVesting());

//			Check if EC csf is on for this contract.
        	setParticipationSection(contractNumber,employee,f,currentContract);
        	request.getSession(false).setAttribute(ELIGIBILITY_MONEY_TYPE_LIST_ATTRIBUTE,f.getEligibilityServiceMoneyTypes());    	
            if (!isAccessibleForEmployee(employee, userProfile)) {
                return redirectForNotAccessible();
            }
            
            checkPartialParticipantStatus(employee, f);
            
            // set the View Salary data
            final Employee employeeForUI = getEmployeeForUI(employee);
            f.setUp(employeeForUI, userProfile);
            
            request.setAttribute(CensusConstants.EMPLOYEE_ATTRIBUTE, employeeForUI);
            
            // store the cloned form for change tracking
            f.storeClonedForm();
            // save values for tracking change on each save attempt
            f.saveInputValues();
            
            if(f.isEligibilityCalcCsfOn()) {
            if (employee.getEmployeePlanEntryVoList() == null
    				|| employee.getEmployeePlanEntryVoList().isEmpty()) {
    		    ArrayList eligibleEmployeePlanEntryVoList=new ArrayList();
    			int totalNumberOfMoneyTypes = f
    					.getEligibilityServiceMoneyTypes().size();
    			for (int i = 0; i < totalNumberOfMoneyTypes; i++) {
    				EligibilityCalculationMoneyType currentECalcMoneyType = f
    						.getEligibilityServiceMoneyTypes().get(i);
    				EmployeePlanEntryVO employeePlanEntryVo = getEmployeeplanEntryVo(
    						request, employee, f, currentECalcMoneyType
    								.getMoneyTypeId());
    				eligibleEmployeePlanEntryVoList.add(employeePlanEntryVo);
    			}
    			employee.setEligibleEmployeePlanEntryVoList(eligibleEmployeePlanEntryVoList);
    			EmployeeSnapshotErrorUtil.getInstance().setChangedSet(f.getEligibilityMoneyTypeFieldNames());
    		}
            }
            
            
            validateEmployee( f, request, employee);
          
            //Set OBDS link to display or not
    		f.setOnlineBeneficiaryLinkDisplayed(f
    				.displayBeneficiaryLinkChoose(userProfile.getCurrentContract()
    						.getContractNumber()));
            return forwardToInput( request, f, State.Normal);
        } catch (SystemException e) {
            logger.error("Fail to get Employee information: ", e);
            removeFormUponExit( request);
            throw e;
        } catch (NumberFormatException e) {
            logger.error("ProfileId or ContractId is not in correct format: profileId = "
                    + profileId + " contractId = " + contractNumber, e);
            removeFormUponExit( request);
            throw e;
        }
    }

    /**
     * Continue edit after warning page
     *
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     * @throws SystemException
     */
    
    @RequestMapping(value ="/editEmployeeSnapshot/",params={"action=continueEdit"},  method ={RequestMethod.GET,RequestMethod.POST}) 
   	public String doContinueEdit(@Valid @ModelAttribute("editEmployeeForm") EditEmployeeSnapshotForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
   		   	throws IOException,ServletException, SystemException {
    	if (bindingResult.hasErrors()) {
    		String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if (errDirect != null) {
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("error");
    		}
    	}
    	((EditEmployeeSnapshotForm) actionForm).setSpecialPageType(null);
        return forwardToInput( request, (EditEmployeeSnapshotForm) actionForm, State.Normal);
    }
    

    /**
     * Cancel the edit, go back to the source page
     *
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     * @throws SystemException
     */
    
    @RequestMapping(value ="/editEmployeeSnapshot/",params={"action=employeeStatusHistory"},  method = {RequestMethod.POST,RequestMethod.GET}) 
   	public String doEmployeeStatusHistory(@Valid @ModelAttribute("editEmployeeForm") EditEmployeeSnapshotForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
   		   	throws IOException,ServletException, SystemException {
    	if (bindingResult.hasErrors()) {
    		String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if (errDirect != null) {
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("error");
    		}
    	}
    	    if(logger.isDebugEnabled()) {
    		    logger.debug("entry -> doEmployeeStatusHistory");
    		    
    	    }
 
    	    String profileId = actionForm.getProfileId();
    	    String snapshotUrl = "/do/employeeStatusHistory/?profileId=" + profileId;
    	    //return mapping.findForward(snapshotUrl);
    	    return snapshotUrl;

    }

    /**
     * Cancel the edit, go back to the source page
     *
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     * @throws SystemException
     */
   
  @RequestMapping(value ="/editEmployeeSnapshot/",params={"action=cancel"},  method ={RequestMethod.POST})
    public String doCancel( @Valid @ModelAttribute("editEmployeeForm") EditEmployeeSnapshotForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
   		   	throws IOException,ServletException, SystemException {
	  if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("error");
			}
		}
        EmployeeSnapshotForm form = (EmployeeSnapshotForm) actionForm;
        
        String forward =forwardToSource(request,actionForm);
        return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
        
    }

    /**
     * Reset the edit, reload the values from database
     *
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     * @throws SystemException
     */
    @RequestMapping(value ="/editEmployeeSnapshot/",params={"action=reset"},  method ={RequestMethod.POST}) 
   	public String doReset (@Valid @ModelAttribute("editEmployeeForm") EditEmployeeSnapshotForm f, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
   	throws IOException,ServletException, SystemException {
    	if (bindingResult.hasErrors()) {
    		String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if (errDirect != null) {
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("error");
    		}
    	}
        UserProfile userProfile = getUserProfile(request);
        Contract currentContract = userProfile.getCurrentContract();
        int  contractId= currentContract.getContractNumber();
        Employee employee = serviceFacade.getEmployee(Long.parseLong(f.getProfileId()), userProfile, new Date(), true);
		// clear the vesting information
		f.setVestingInformation(null);
        f.setUp(getEmployeeForUI(employee), userProfile);
        f.setSpecialPageType(null);
        
        if (f.isDisplayLongTermPartTimeAssessmentYearField()) {
	        int longTermPartTimeAssessmentYear = LongTermPartTimeAssessmentUtil.getInstance()
					.evaluateLongTermPartTimeAssessmentYear(contractId, Integer.parseInt(f.getProfileId()),
							null);
	        f.setLongTermPartTimeAssessmentYear(longTermPartTimeAssessmentYear);
        }
        // Must set the employee back for the UI to show the vesting section correctly.
        final Employee employeeForUI = getEmployeeForUI(employee);     
        request.setAttribute(CensusConstants.EMPLOYEE_ATTRIBUTE, employeeForUI);

        return forwardToInput( request, f, State.Normal);
    }

	protected String doCommon(
			EmployeeSnapshotForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			SystemException {
		// this is called by show/hide previous value
		// do a validation on the data so that the error/warnings
		// are shown
		EditEmployeeSnapshotForm f = (EditEmployeeSnapshotForm) actionForm;

		long profileId = Long.parseLong(f.getProfileId());
        
		Employee employee = serviceFacade.getEmployee(profileId,
			SessionHelper.getUserProfile(request), new Date(), f.isShowVesting());
        final Employee employeeForUI = getEmployeeForUI(employee);     
        request.setAttribute(CensusConstants.EMPLOYEE_ATTRIBUTE, employeeForUI);
        UserProfile userProfile = SessionHelper.getUserProfile(request);
        Contract currentContract = userProfile.getCurrentContract();
        int  contractId= currentContract.getContractNumber();
        setParticipationSection(contractId,employee,f,currentContract);
        
        f.updateEmployee(employee, true);
		validateEmployee( f, request, employee);
		return forwardToInput( request,
				(EditEmployeeSnapshotForm) actionForm, State.Normal);
    }

	
	/**
	 * Method used to set the plan eligible Indicator
	 * 
	 * @param employee - 
	 *            Employee Details
	 * @param contractId -
	 *            Contract Id
	 * @throws SystemException
	 */
	private void setPlanEligibleIndicator(Employee employee, int contractId)
			throws SystemException {

		EmployeeVestingVO employeeVestingVO = employee.getEmployeeVestingVO();
		Date eligibilityDate = employeeVestingVO.getEligibilityDate();
		String planEligibleIndicator = employeeVestingVO.getPlanEligibleInd();

		if (eligibilityDate != null
				&& StringUtils.isBlank(planEligibleIndicator)
				&& isAutoEnrollmentON(contractId)) {
			// Turn On Plan Eligible Indicator & set Auto Plan Eligible Indicator as
			// true.
			employeeVestingVO.setPlanEligibleInd(ServiceFeatureConstants.YES);
			employee.setAutoPlanEligibleIndicator(true);
		}
	}

	/**
	 * Method used to find for Auto Enrollment ON (or) OFF
	 * 
	 * @param contractId -
	 *            Contract Id
	 * @return autoEnrollmentStatus - Auto Enrollment Status
	 * @throws SystemException
	 */
	private boolean isAutoEnrollmentON(int contractId) throws SystemException {

		boolean autoEnrollmentStatus = false;
		ContractServiceFeature ezStartContractServiceFeature = null;
		ContractServiceDelegate contractServiceDelegate = ContractServiceDelegate
				.getInstance();

		try {
			ezStartContractServiceFeature = contractServiceDelegate.getContractServiceFeature(contractId,
					ServiceFeatureConstants.AUTO_ENROLLMENT_FEATURE);
		} catch (ApplicationException applicationException) {
			throw new SystemException(applicationException,
					"Error thrown while calling EZStart Contract Service Feature");
		}

		if (ServiceFeatureConstants.YES.equalsIgnoreCase(ezStartContractServiceFeature.getValue())) {
			autoEnrollmentStatus = true;
		}

		return autoEnrollmentStatus;
	}
	
	private int removeErrors(EmployeeValidationErrors errors){
		List<EmployeeValidationError> errorlist = errors.getErrors();
		int count = 0;
			for (EmployeeValidationError error : errorlist) {
				if (error.getType().compareTo(
						EmployeeValidationError.ErrorType.error) == 0) {
					count++;
				}
			}
			return count;
	}

	
	@Autowired
    private PSValidatorFWError psValidatorFWError;  

	@InitBinder
	protected void initBinder(HttpServletRequest request,
				ServletRequestDataBinder  binder) {
		binder.addValidators(psValidatorFWError);
	}
	
	

	
}
