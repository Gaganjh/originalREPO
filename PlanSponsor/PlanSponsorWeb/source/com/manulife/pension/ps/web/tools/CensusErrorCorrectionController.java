package com.manulife.pension.ps.web.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.ObjectUtils;
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

import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.platform.web.controller.ControllerRedirect;
import com.manulife.pension.platform.web.taglib.util.LabelValueBean;
import com.manulife.pension.ps.service.submission.valueobject.CensusSubmissionItem;
import com.manulife.pension.ps.service.submission.valueobject.SubmissionValidationBundle;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.census.util.CensusErrorCorrectionErrorUtil;
import com.manulife.pension.ps.web.census.util.CensusLookups;
import com.manulife.pension.ps.web.census.util.CensusUtils;
import com.manulife.pension.ps.web.census.util.EmployeeServiceFacade;
import com.manulife.pension.ps.web.census.util.EmployeeSnapshotValidationErrors;
import com.manulife.pension.ps.web.controller.PsAutoController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.delegate.SubmissionServiceDelegate;
import com.manulife.pension.ps.web.tools.CensusErrorCorrectionForm.DisplayProperty;
import com.manulife.pension.ps.web.tools.CensusErrorCorrectionForm.SpecialPageType;
import com.manulife.pension.ps.web.tools.util.LockManager;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.employee.util.EmployeeData;
import com.manulife.pension.service.employee.util.EmployeeData.Property;
import com.manulife.pension.service.employee.util.EmployeeValidationError;
import com.manulife.pension.service.employee.util.EmployeeValidationErrorCode;
import com.manulife.pension.service.employee.util.EmployeeValidationErrors;
import com.manulife.pension.service.employee.valueobject.UserIdType;
import com.manulife.pension.util.Pair;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.validator.ValidationError;


@Controller
@RequestMapping(value ="/tools")
@SessionAttributes({"errorCorrectionForm"})

public class CensusErrorCorrectionController extends PsAutoController {
	@ModelAttribute("errorCorrectionForm")
	public CensusErrorCorrectionForm populateForm() {
		return new CensusErrorCorrectionForm();
	}
	private interface Forward {
		String CENSUS_DETAILS = "censusDetails";

		String TOOLS = "tools";

		String REFRESH = "refresh";

		String LOCK_ERROR = "lockError";

		String SPECIAL_HANDLING_PAGE = "specialHandling";
	}
	public static final String BLANK = "";
	public static final String INPUT = "input";
	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put(INPUT, "/tools/uploadCensusErrorCorrection.jsp");
		forwards.put(Forward.SPECIAL_HANDLING_PAGE, "/tools/uploadCensusErrorCorrectionSpecial.jsp");
		forwards.put(Forward.REFRESH, "redirect:/do/tools/uploadCensusErrorCorrection/");
		forwards.put(Forward.CENSUS_DETAILS, "redirect:/do/tools/viewCensusDetails/");
		forwards.put(Forward.TOOLS, "redirect:/do/tools/toolsMenu/");
		forwards.put(Forward.LOCK_ERROR, "redirect:/do/tools/submissionHistory/");
	}

	



	private static final EmployeeValidationErrorCode[] RECORD_LEVEL_SPECIAL_HANDLING_ERROR_CODES = new EmployeeValidationErrorCode[] { 
        EmployeeValidationErrorCode.CancelledParticipant, // 31
        EmployeeValidationErrorCode.CancelledEmployee  //30
	};

	private static final EmployeeValidationErrorCode[] FIELD_LEVEL_SPECIAL_HANDLING_ERROR_CODES	 = new EmployeeValidationErrorCode[] {
            EmployeeValidationErrorCode.InvalidSSN, // 11
            EmployeeValidationErrorCode.InvalidEmployeeIdSortOptionEE, // 137
            EmployeeValidationErrorCode.MissingSSN, // 14
            EmployeeValidationErrorCode.EmployeeIdSSNMismatch, // 136
            EmployeeValidationErrorCode.MissingEmployeeId, // 15
            EmployeeValidationErrorCode.DuplicateSubmittedSSN, // 89
            EmployeeValidationErrorCode.MultipleDuplicateEmployeeIdSortOptionEE, // 210
			EmployeeValidationErrorCode.DuplicateEmployeeIdAccountHolderSortOptionEE, // 208
			EmployeeValidationErrorCode.DuplicateEmployeeIdNonAccountHolderSortOptionEE, // 206
            EmployeeValidationErrorCode.DuplicateSubmittedEmployeeId, // 16
            EmployeeValidationErrorCode.MultipleDuplicateEmployeeIdSortOptionNotEE, // 211
			EmployeeValidationErrorCode.DuplicateEmployeeIdAccountHolderSortOptionNotEE, // 209
			EmployeeValidationErrorCode.DuplicateEmployeeIdNonAccountHolderSortOptionNotEE, // 207
            EmployeeValidationErrorCode.MissingSSNNewEmployeeEE, // 17
			EmployeeValidationErrorCode.SimilarSSN, // 13
			EmployeeValidationErrorCode.DuplicateSubmittedEmailAddress, //214
			EmployeeValidationErrorCode.DuplicateEmailAddress //213
	};
	

	private static final String WARNING_PAGE_PARAMETER = "showWarningPage";

	private static final String SPECIAL_ERROR_REQUEST_ATTRIBUTE = "specialError";

	private static final String SPECIAL_ERROR_MESSAGE_REQUEST_ATTRIBUTE = "specialErrorMsg";

	protected static EmployeeServiceFacade serviceFacade = new EmployeeServiceFacade();
	
    protected String preExecute(
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			SystemException {

		UserProfile userProfile = SessionHelper.getUserProfile(request);

		/*
		 * Make sure the login user has the necessary permission to access this
		 * page.
		 */
		if (!(userProfile.isSubmissionAccess() || userProfile.isInternalUser())
				|| !userProfile.isAllowedUploadSubmissions()) {
			Collection<GenericException> errors = new ArrayList<GenericException>();
			errors.add(new GenericException(
					ErrorCodes.EMPLOYEE_INVALID_PERMISSION));
			setErrorsInSession(request, errors);
			return forwards.get( Forward.TOOLS);
		}

		CensusErrorCorrectionForm form = (CensusErrorCorrectionForm) actionForm;

		/*
		 * Refresh the lock on every action.
		 */
		if (form.getSubmissionId() != null && form.getContractId() != null
				&& form.getSubmissionCaseTypeCode() != null) {
			String lockErrorForward = acquireLockOrErrorForward(
					request, form);
			if (lockErrorForward != null) {
				return lockErrorForward;
			}
		}

		return null;
	}
    

    @RequestMapping(value ="/uploadCensusErrorCorrection/",  method =  {RequestMethod.GET}) 
    public String doDefault(@Valid @ModelAttribute("errorCorrectionForm") CensusErrorCorrectionForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get(INPUT);
			}
		}
		String forward = preExecute(actionForm, request, response);
		if (forward != null) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		CensusErrorCorrectionForm form = (CensusErrorCorrectionForm) actionForm;

		List<GenericException> errors = new ArrayList<GenericException>();

		List<ValidationError> validationErrors = new ArrayList<ValidationError>();

		String submissionIdStr = request
				.getParameter(Constants.SUBMISSION_ID_PARAMETER);
		String sequenceNumberStr = request
				.getParameter(Constants.SEQUENCE_NUMBER_PARAMETER);
		String contractIdStr = request
				.getParameter(Constants.CONTRACT_NUMBER_PARAMETER);
        String employerDesignatedId = request
                .getParameter(Constants.EMPLOYER_DESIGNATED_ID_PARAMETER);
        
		String warningPageStr = StringUtils.trimToEmpty(request
				.getParameter(WARNING_PAGE_PARAMETER));
		boolean isWarningPage = Constants.YES.equals(warningPageStr);
		
		if (submissionIdStr == null || sequenceNumberStr == null
				|| contractIdStr == null) {
			// return an error if submission Id is null
			errors.add(new GenericException(
					ErrorCodes.SUBMISSION_HAS_NO_VALID_DATA));
		} else {
			/*
			 * Prepare the action form that will have both the submission
			 * details and the CSDB data (if present)
			 */
			Integer submissionId = Integer.valueOf(submissionIdStr);
			Integer contractId = Integer.valueOf(contractIdStr);
            Integer sequenceNumber = Integer.valueOf(sequenceNumberStr);
			boolean isSameForm = true;

			/*
			 * We don't reset similar SSN from the DB unless someone has gone
			 * off the page into another record.
			 */
			if (!ObjectUtils.equals(form.getSubmissionId(), submissionId)
					|| !ObjectUtils.equals(form.getContractId(), contractId)
					|| !ObjectUtils.equals(form.getSequenceNumber(),
                            sequenceNumber)) {
				form.setSimilarSsnAccepted(false);
				isSameForm = false;
			}

			if (!isWarningPage || !isSameForm) {
				/*
				 * Initialize the form only if we are not in the Warning page, this is
				 * necessary because the Warning page doesn't really save the
				 * data. So, if we reload the data from DB in such case, we will
				 * get the value from DB instead of the one user inputs.
				 */
				SubmissionValidationBundle<EmployeeData, EmployeeData, EmployeeValidationErrors> bundle = SubmissionServiceDelegate
						.getInstance().retrieveAndValidateCensusSubmission(
								contractId, submissionId, employerDesignatedId, sequenceNumber);
				
				initializeForm(request, bundle, form, submissionId, contractId,
                        employerDesignatedId, sequenceNumber);
				
				String lockErrorForward = acquireLockOrErrorForward(
						request, form);
				if (lockErrorForward != null) {
					return lockErrorForward;
				}
			}

			if (form.isSubmissionProcessed()) {
				/*
				 * If submission is already processed (i.e. PROCESS_STATUS_CODE =
				 * COMPLETE or CANCELLED), we don't need to do any validation.
				 */
				form.setDisabled(true);
			} else {
				
				List<ValidationError> businessValidationErrors = CensusErrorCorrectionErrorUtil
						.getInstance().convert(form.getErrors());
				validationErrors.addAll(businessValidationErrors);
				request.setAttribute("validationErrors",
						new EmployeeSnapshotValidationErrors(validationErrors));

				if (!form.getErrors().isEmpty()) {

					/*
					 * First, check if we have duplicate SSN error. The
					 * specialErrorList contains a list of error codes and the
					 * corresponding property for the first special error.
					 */
					List<Pair<Property, EmployeeValidationError>> specialErrorList = getSpecialError(form);
//					boolean enableEmployerProvidedEmail = false;
					if (specialErrorList != null) {
						/*
						 * IMPORTANT: If we have special error, we need to deal
						 * with it here.
						 */
						for (Map.Entry<String, DisplayProperty> displayPropertyEntry : form
								.getDisplayProperties().entrySet()) {

							String propertyName = displayPropertyEntry.getKey();
							DisplayProperty displayProperty = displayPropertyEntry
									.getValue();

							boolean isSpecialErrorProperty = false;
							for (Pair<Property, EmployeeValidationError> errorPair : specialErrorList) {
								
								if (errorPair.getFirst() != null) {
									/*
									 * If the property is null, it means the
									 * entire record is rejected, we need to
									 * make sure the display properties are all
									 * set to false.
									 */
									String errorPropertyName = errorPair
											.getFirst().getName();
									if (propertyName.equals(errorPropertyName)) {
										isSpecialErrorProperty = true;
										break;
									}
								}
							}
							if (!isSpecialErrorProperty) {
								displayProperty.resetWarningField();
								displayProperty.resetErrorField();
								displayProperty.setInitialDisplay(false);
							}
						}

						EmployeeValidationError employeeValidationError = specialErrorList
								.get(0).getSecond();
						Property errorProperty = specialErrorList.get(0)
								.getFirst();
						EmployeeValidationErrorCode errorCode = employeeValidationError
								.getErrorCode();
						request.setAttribute(SPECIAL_ERROR_REQUEST_ATTRIBUTE,
								employeeValidationError);
						ValidationError validationError = CensusErrorCorrectionErrorUtil
								.getInstance().convert(errorProperty,
										employeeValidationError);
						request.setAttribute(
								SPECIAL_ERROR_MESSAGE_REQUEST_ATTRIBUTE,
								validationError);
						form.setDisabled(true, errorCode);
						return forwards.get(Forward.SPECIAL_HANDLING_PAGE);
					}
				}
			}
		}

		return forwards.get(INPUT);
	}

	/**
	 * This callback method is called when the "Accept" button is clicked on the
	 * similarSSN, duplicateSubmittedSSN, duplicateEmployeeIdNonAccountHolderSortOptionEE, and
	 * duplicateEmployeeIdNonAccountHolderSortOptionNotEE pages. 
     * - For similarSSN, it just sets the flag in the form and refreshes the page.
     * - For duplicateSubmittedSSN it discards all other duplicate submitted submission items.
     * - For duplicateEmployeeIdNonAccountHolderSortOptionEE it changes the employeeId of the
     *   matching record to a new generated employeeId.
     * - For duplicateEmployeeIdNonAccountHolderSortOptionNotEE it changes the employeeId
     *   of the matching record to blank.
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 * @throws ApplicationException 
	 */
    @RequestMapping(value ="/uploadCensusErrorCorrection/" ,params={"action=Accept"},method =  {RequestMethod.POST}) 
    public String doAccept (@Valid @ModelAttribute("errorCorrectionForm") CensusErrorCorrectionForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException, ApplicationException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get(INPUT);
			}
		}
	

		logger.debug("entry -> doAccept");
		String forward = preExecute(actionForm, request, response);
		if (forward != null) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		  UserProfile userProfile = SessionHelper.getUserProfile(request);

		if (SpecialPageType.duplicateSubmittedSSN.equals(actionForm.getSpecialPageType())) {
			EmployeeValidationError theError = CensusErrorCorrectionErrorUtil
					.getInstance().getEmployeeValidationError(actionForm.getErrors(),
							Property.SOCIAL_SECURITY_NUMBER,
							EmployeeValidationErrorCode.DuplicateSubmittedSSN);
			List<EmployeeData> employeeList = theError.getEmployees();
            for (EmployeeData submittedEmployee : employeeList) {
                /*
                 * Discard all other duplicate submitted submission items
                 */
                SubmissionServiceDelegate.getInstance().removeCensusSubmissionItem(
                        actionForm.getContractId(), actionForm.getSubmissionId(),
                        submittedEmployee.getSequenceNumber(),
                        String.valueOf(userProfile.getPrincipal().getProfileId()));
            }
			
		} else if (SpecialPageType.similarSsn.equals(actionForm.getSpecialPageType())) {
			actionForm.setSimilarSsnAccepted(true);
		
		// 206
		} else if (SpecialPageType.duplicateEmployeeIdNonAccountHolderSortOptionEE
				.equals(actionForm.getSpecialPageType())) {
			
			EmployeeValidationError theError = CensusErrorCorrectionErrorUtil
				.getInstance().getEmployeeValidationError(actionForm.getErrors(),
						Property.EMPLOYEE_NUMBER,
						EmployeeValidationErrorCode.DuplicateEmployeeIdNonAccountHolderSortOptionEE);
			
			EmployeeData employee = theError.getEmployees().get(0);
			String newEmployeeId = (String)(theError.getParams()[0]);
			
            String applicationCode = SubmissionServiceDelegate.getInstance().getApplicationCode(
            		actionForm.getSubmissionId());

			serviceFacade.updateEmployeeId(
			        employee.getProfileId(),
			        employee.getContractId(),
			        newEmployeeId,
			        userProfile.getPrincipal().getProfileId(),
			        userProfile.isInternalUser() ? UserIdType.UP_INTERNAL : UserIdType.UP_EXTERNAL,
			        applicationCode);
			
			actionForm.setIgnoreEmployeeIdRules(true);
		
		// 207	
		} else if (SpecialPageType.duplicateEmployeeIdNonAccountHolderSortOptionNotEE
				.equals(actionForm.getSpecialPageType())) {
			
			actionForm.resetEmployeeNumber();
			
			EmployeeValidationError theError = CensusErrorCorrectionErrorUtil
				.getInstance().getEmployeeValidationError(actionForm.getErrors(),
						Property.EMPLOYEE_NUMBER,
						EmployeeValidationErrorCode.DuplicateEmployeeIdNonAccountHolderSortOptionNotEE);
		
			EmployeeData employee = theError.getEmployees().get(0);
			
            String applicationCode = SubmissionServiceDelegate.getInstance().getApplicationCode(
            		actionForm.getSubmissionId());

			serviceFacade.updateEmployeeId(
			        employee.getProfileId(),
			        employee.getContractId(),
			        BLANK,
			        userProfile.getPrincipal().getProfileId(),
			        userProfile.isInternalUser() ? UserIdType.UP_INTERNAL : UserIdType.UP_EXTERNAL,
			        applicationCode);
			
			actionForm.setIgnoreEmployeeIdRules(true);
		}

		 forward = doSave( actionForm, bindingResult, request, response);

		logger.debug("exit <- doAccept");

		return forward;
	}

	/**
	 * This callback method is called when the "Discard" button is clicked on
	 * the special SSN page or the main correction page. It essentially discards
	 * (cancels) the census record.
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 * @throws ApplicationException 
	 */
    @RequestMapping(value ="/uploadCensusErrorCorrection/", params={"action=Discard"} , method =  {RequestMethod.POST}) 
    public String doDiscard (@Valid @ModelAttribute("errorCorrectionForm") CensusErrorCorrectionForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get(INPUT);
			}
		}

		logger.debug("entry -> doDiscard");
		String forward = preExecute(form, request, response);
		if (forward != null) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		UserProfile userProfile = SessionHelper.getUserProfile(request);

		/*
		 * Discard the submission item.
		 */
		SubmissionServiceDelegate.getInstance().removeCensusSubmissionItem(
				form.getContractId(), form.getSubmissionId(),
				form.getSequenceNumber(),
                String.valueOf(userProfile.getPrincipal().getProfileId()));
        
        /*
         * For duplicate submitted SSN records, if there is only one more duplicate item
         * (after discarding this item), the remaining item has to be re-validated
         */
        List<EmployeeData> employeeList = new ArrayList<EmployeeData>();
        EmployeeValidationError theError;
        if (SpecialPageType.duplicateSubmittedSSN.equals(form.getSpecialPageType())) {
            
            theError = CensusErrorCorrectionErrorUtil.getInstance().getEmployeeValidationError(
                        form.getErrors(),
                        Property.SOCIAL_SECURITY_NUMBER,
                        EmployeeValidationErrorCode.DuplicateSubmittedSSN);
            
            
            employeeList = theError.getEmployees();
            
            if (employeeList.size() == 1) {
                /*
                 * Retrieve remaining duplicate employee data from SDB
                 */
                EmployeeData submittedEmployeeData = 
                SubmissionServiceDelegate.getInstance().getSTPEmployeeData(
                        employeeList.get(0).getContractId(), 
                        employeeList.get(0).getSubmissionId(), 
                        employeeList.get(0).getSequenceNumber());   
                
                /*
                 * Populate extra fields needed for saving
                 */ 
                submittedEmployeeData.setUserTypeCode(userProfile.isInternalUser() ? 
                        UserIdType.UP_INTERNAL : UserIdType.UP_EXTERNAL);
                submittedEmployeeData.setCreatedUserFirstName(userProfile.getPrincipal().getFirstName());
                submittedEmployeeData.setCreatedUserLastName(userProfile.getPrincipal().getLastName());
                submittedEmployeeData.setConfirmed(true);
                
                /*
                 * Re-validate and save the one remaining duplicate submission item.
                 */
                try {
					SubmissionServiceDelegate.getInstance().validateAndSaveCensusSubmissionItem(
					        submittedEmployeeData,
					        userProfile.getPrincipal().getUserName(),
					        userProfile.getPrincipal().getProfileId(),
					        true, false, false);
				} catch (ApplicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }

		logger.debug("exit <- doDiscard");

		return getActionForwardForNextRecord( form);
	}

	/**
	 * Save the records and validate.
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 * @throws ApplicationException 
	 */
    @RequestMapping(value ="/uploadCensusErrorCorrection/", params={"action=Save"}  , method =  {RequestMethod.POST}) 
    public String doSave (@Valid @ModelAttribute("errorCorrectionForm") CensusErrorCorrectionForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException, ApplicationException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get(INPUT);
			}
		}

		logger.debug("entry -> doSave");
		String forward = preExecute(form, request, response);
		if (forward != null) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (SpecialPageType.duplicateSubmittedEmail.equals(form
				.getSpecialPageType())) {
			UserProfile userProfile = SessionHelper.getUserProfile(request);

			EmployeeValidationError theError = CensusErrorCorrectionErrorUtil
					.getInstance()
					.getEmployeeValidationError(
							form.getErrors(),
							Property.EMPLOYER_PROVIDED_EMAIL,
							EmployeeValidationErrorCode.DuplicateSubmittedEmailAddress);
			if (theError != null) {
				EmployeeData employee = theError.getEmployees().get(0);
				SubmissionServiceDelegate
						.getInstance()
						.deleteDuplicateEmailAddressesOnFile(
								employee.getContractId(),
								employee.getSubmissionId(),
								form.getSequenceNumber(),
								form.getFormValue("employerProvidedEmail"),
								String.valueOf(userProfile
										.getAbstractPrincipal().getProfileId()));
			}
		} else if(SpecialPageType.duplicateEmail.equals(form
				.getSpecialPageType())) {
			
			UserProfile userProfile = SessionHelper.getUserProfile(request);
			
			EmployeeValidationError theError = CensusErrorCorrectionErrorUtil
				.getInstance()
				.getEmployeeValidationError(
						form.getErrors(),
						Property.EMPLOYER_PROVIDED_EMAIL,
						EmployeeValidationErrorCode.DuplicateEmailAddress);
			if (theError != null) {
				EmployeeData employee = theError.getEmployees().get(0);
				
				if (employee.getEmployerProvidedEmail() != null && 
						employee.getEmployerProvidedEmail().toLowerCase()
								.equalsIgnoreCase(form.getFormValue("employerProvidedEmail"))) {

					String userId = String.valueOf(userProfile
							.getAbstractPrincipal().getProfileId());
					String userTypeCode = userProfile.isInternalUser() ? UserIdType.UP_INTERNAL
									: UserIdType.UP_EXTERNAL;
					String confirmedIndicator = employee.isConfirmed() == true ? "Y":"N";
					String updateEmployerProvidedEmailAddress = null;
					serviceFacade.
							updateEmployerProvidedEmailAddress(employee.getProfileId(), employee.getContractId(), 
									updateEmployerProvidedEmailAddress, employee.getSourceChannelCode(), confirmedIndicator, userId, userTypeCode);
				}
			}
		}
		
		forward = validateAndRedirectIfFailed( form,
				 request, false);

		if (forward == null) {
			forward = getActionForwardForNextRecord( form);
		}

		logger.debug("exit <- doSave");

		return forward;
	}

	/**
	 * Save the records and validate.
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 * @throws ApplicationException 
	 */
    @RequestMapping(value ="/uploadCensusErrorCorrection/", params={"action=Confirm"}, method =  {RequestMethod.POST}) 
    public String doConfirm (@Valid @ModelAttribute("errorCorrectionForm") CensusErrorCorrectionForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException, ApplicationException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get(INPUT);
			}
		}

		logger.debug("entry -> doConfirm");
		String forward = preExecute(form, request, response);
		if (forward != null) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		forward = validateAndRedirectIfFailed( form,
				request, true);

		if (forward == null) {
			forward = getActionForwardForNextRecord(form);
		}

		logger.debug("exit <- doConfirm");

		return forward;
	}

   @RequestMapping(value ="/uploadCensusErrorCorrection/" ,params={"action=NextRecord"}  , method =  {RequestMethod.POST}) 
    public String doNextRecord (@Valid @ModelAttribute("errorCorrectionForm") CensusErrorCorrectionForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get(INPUT);
			}
		}

		logger.debug("entry -> doNextRecord");
		String forward = preExecute(form, request, response);
		if (forward != null) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		forward = getActionForwardForNextRecord( form);
		
		logger.debug("exit <- doNextRecord");

		return forward;
	}

   @RequestMapping(value ="/uploadCensusErrorCorrection/", params={"action=Edit"} , method =  {RequestMethod.POST}) 
   public String doEdit (@Valid @ModelAttribute("errorCorrectionForm") CensusErrorCorrectionForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
   throws IOException,ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get(INPUT);
			}
		}

		logger.debug("entry -> doEdit");
		String forwardVal = preExecute(form, request, response);
		if (forwardVal != null) {
			return StringUtils.contains(forwardVal, '/') ? forwardVal : forwards.get(forwardVal);
		}
		/*
		 * Set the warning page flag to false because the refresh on doDefault
		 * won't reinitialize the form.
		 */
		form.setWarningPage(false);

		ControllerRedirect forward = getActionRedirect(
				form.getContractId(), form.getSubmissionId(), 
                form.getEmployerDesignatedId(), form.getSequenceNumber());

		logger.debug("exit <- doEdit");

		return forward.getPath();
	}

	/**
	 * Validate the submission and returns a forward to redirect to the same
	 * page if validation failed. Otherwise, return a null forward. When the
	 * caller receives a null forward, it should redirect to the next record.
	 * The form may contain stale data in this case and needs to be refreshed on
	 * the next load (which happens when it's the next record).
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 * @throws ApplicationException 
	 */
    
   protected String validateAndRedirectIfFailed(
			AutoForm actionForm, HttpServletRequest request,
			boolean ignoreWarnings) throws IOException, ServletException,
			SystemException, ApplicationException {

		logger.debug("entry -> validateAndForward");

		ControllerRedirect forward = null;

		boolean showWarningPage = false;
		CensusErrorCorrectionForm form = (CensusErrorCorrectionForm) actionForm;
		
		EmployeeValidationErrors errors = form.getErrors();		
		UserProfile userProfile = SessionHelper.getUserProfile(request);
		EmployeeData submittedEmployeeData = form.getEmployeeData(userProfile);
		
        SubmissionValidationBundle<EmployeeData, EmployeeData, EmployeeValidationErrors> bundle;
		
		// Check for special error edits
		if (SpecialPageType.multipleDuplicateEmployeeIdSortOptionNotEE
				.equals(form.getSpecialPageType()) ||
			SpecialPageType.duplicateEmployeeIdAccountHolderSortOptionNotEE
				.equals(form.getSpecialPageType()) ||
			SpecialPageType.duplicateEmployeeIdNonAccountHolderSortOptionNotEE
				.equals(form.getSpecialPageType()) ||
			SpecialPageType.duplicateEmployeeIdNonAccountHolderSortOptionEE
				.equals(form.getSpecialPageType())) {
		    
			// Save submission item containing edited employeeId
			bundle = SubmissionServiceDelegate.getInstance().validateAndSaveCensusSubmissionItem(
					submittedEmployeeData,
					userProfile.getPrincipal().getUserName(),
					userProfile.getPrincipal().getProfileId(),
					true, false, form.isIgnoreEmployeeIdRules());
			
		} else {
			
			// Save submission item from regular error page.
			bundle = SubmissionServiceDelegate.getInstance().validateAndSaveCensusSubmissionItem(
					submittedEmployeeData,
					userProfile.getPrincipal().getUserName(),
					userProfile.getPrincipal().getProfileId(),
					ignoreWarnings, form.isSimilarSsnAccepted(), false);
		}
		

		if (!bundle.getValidationErrors().hasError()) {
			if (bundle.getValidationErrors().hasWarning()) {
				if (ignoreWarnings) {
					/*
					 * Return null means that we are done with the record,
					 * caller will redirect to the next record.
					 */
					return null;
				} else {
					showWarningPage = true;
				}
			} else {
				return null;
			}
		}


		initializeForm(request, bundle, form, form.getSubmissionId(), form
				.getContractId(), form.getEmployerDesignatedId(), form.getSequenceNumber());
		
		/*
		 * If there remains validation error or warnings and not ignore
		 * warnings, return to the same page and let the user fix it.
		 */
		forward = getActionRedirect( 
                form.getContractId(), form.getSubmissionId(), 
                form.getEmployerDesignatedId(), form.getSequenceNumber());

		if (showWarningPage) {
			/*
			 * If there remains validation error or warnings and not ignore
			 * warnings, return to the same page and let the user fix it.
			 */
			((ControllerRedirect) forward).addParameter(WARNING_PAGE_PARAMETER,
					Constants.YES);
			form.setWarningPage(true);
		}

		logger.debug("exit <- validateAndForward");

		return forward.getPath();
	}


	/**
	 * Advances the census record by 1 and returns the forward of the error
	 * correction page. If no more error is found, returns the forward of the
	 * view census details page.
	 * 
	 * @param mapping
	 * @param form
	 * @return
	 * @throws SystemException
	 */
	protected String getActionForwardForNextRecord(
			 CensusErrorCorrectionForm form)
			throws SystemException {
		/*
		 * Move onto the next record or back to the details page if the whole
		 * file is clean.
		 */
		Pair<CensusSubmissionItem, Integer> nextItem = SubmissionServiceDelegate
				.getInstance().getNextCensusSubmissionItemInError(
						form.getContractId(), form.getSubmissionId(),
						form.getSourceRecordNo());

		String forward = null;

		if (nextItem != null) {

			CensusSubmissionItem submissionItem = nextItem.getFirst();
			form.setNumberOfErrorsInSubmission(nextItem.getSecond());

			/*
			 * If we have more items to fix, initialize the form in the session.
			 */
			forward = getActionRedirect( 
                    submissionItem.getContractNumber(), submissionItem.getSubmissionId(),
					submissionItem.getEmpId(), submissionItem.getSequenceNumber()).getPath();
            
		} else {
			/*
			 * Otherwise, we return to the census details page.
			 */
			form.setNumberOfErrorsInSubmission(0);
			forward = forwards.get(Forward.CENSUS_DETAILS);
		}

		return forward;
	}

	/**
	 * Returns an action redirect with parameters that can be used to refresh
	 * the page.
	 * 
	 * @param mapping
	 * @param contractId
	 * @param submissionId
	 * @param employerDesignatedId
	 * @return
	 */
	private ControllerRedirect getActionRedirect(
			Integer contractId, Integer submissionId,
			String employerDesignatedId, Integer sequenceNumber) {
		String forward = forwards.get(Forward.REFRESH);
		ControllerRedirect redirect = new ControllerRedirect(forward);
		redirect.addParameter(Constants.SUBMISSION_ID_PARAMETER, submissionId);
		redirect.addParameter(Constants.EMPLOYER_DESIGNATED_ID_PARAMETER,
				employerDesignatedId);
		redirect.addParameter(Constants.CONTRACT_NUMBER_PARAMETER, contractId);
        redirect.addParameter(Constants.SEQUENCE_NUMBER_PARAMETER, sequenceNumber);
		return redirect;
	}

	/**
	 * Initialize the form with the submitted data and online data and the
	 * validation errors.
	 * 
	 * @param form
	 * @param submissionId
	 * @param contractId
	 * @param employerDesignatedId
	 * @throws SystemException
	 */
	private void initializeForm(
			HttpServletRequest request,
			SubmissionValidationBundle<EmployeeData, EmployeeData, EmployeeValidationErrors> bundle,
			CensusErrorCorrectionForm form, Integer submissionId,
			Integer contractId, String employerDesignatedId, Integer sequenceNumber)
			throws SystemException {

		UserProfile userProfile = SessionHelper.getUserProfile(request);

		form.setContractId(contractId);
		form.setSubmissionId(submissionId);
		form.setEmployerDesignatedId(employerDesignatedId);
        form.setSequenceNumber(sequenceNumber);

		EmployeeValidationErrors errors = bundle.getValidationErrors();

		// get mask sensitive information indicator from the system of records
		boolean maskSensitiveInfoInd = false;
		if (bundle.getSystemOfRecord() != null) {
			if (Constants.YES.equals(bundle.getSystemOfRecord()
					.getMaskSensitiveInformationInd()))
				maskSensitiveInfoInd = true;
			else
				maskSensitiveInfoInd = false;
		}

		boolean maskSensitiveInformation = CensusUtils
				.isMaskSensitiveInformation(userProfile, null, maskSensitiveInfoInd);

		form.initializeFormValuesAndDisplayProperties(userProfile
				.getCurrentContract().getParticipantSortOptionCode(), bundle
				.getSubmissionItem(), bundle.getSystemOfRecord(), errors,
				maskSensitiveInformation);
		form.setNumberOfErrorsInSubmission(bundle
				.getRemainingNumberOfErrorRecords());
		form.storeClonedForm();
		setupLookupData(form);
	}

	/**
	 * Populate static data (e.g. drop downs) into the form.
	 * 
	 * @param form
	 */
	private void setupLookupData(CensusErrorCorrectionForm form) throws SystemException {
		Map<String, List<LabelValueBean>> staticData = new HashMap<String, List<LabelValueBean>>();
		// load employment status
		CensusLookups censusLookups = CensusLookups.getInstance();

		staticData.put(EmployeeData.Property.EMPLOYMENT_STATUS.getName(),
				prepareStaticDataForDisplay(censusLookups
						.getEmploymentStatuses()));
		staticData.put(EmployeeData.Property.STATE_CODE.getName(),
				prepareStaticDataForDisplay(censusLookups.getStates()));
		staticData.put(EmployeeData.Property.STATE_OF_RESIDENCE.getName(),
				prepareStaticDataForDisplay(censusLookups.getStates()));
		staticData.put(EmployeeData.Property.COUNTRY_NAME.getName(),
				prepareStaticDataForDisplay(censusLookups.getCountries()));
		staticData.put(EmployeeData.Property.NAME_PREFIX.getName(),
				prepareStaticDataForDisplay(censusLookups.getNamePrefix()));
		staticData.put(EmployeeData.Property.OPT_OUT_IND.getName(),
				prepareStaticDataForDisplay(censusLookups.getOptOutInd()));
        
		form.setLookupData(staticData);
	}

	/**
	 * Insert the error value into the drop down if it is an invalid value.
	 * 
	 * @param form
	 * @param property
	 * @param staticDataList
	 * @return
	 */
	private List<LabelValueBean> prepareStaticDataForDisplay(
			List<LabelValueBean> dataList) {
		
		List<LabelValueBean> staticDataList = new ArrayList<LabelValueBean>(
				dataList);
		
		/*
		 * Make sure user can select empty value in a list box.
		 */
		LabelValueBean firstItem = staticDataList.get(0);
		if (!StringUtils.isEmpty(firstItem.getValue())) {
			staticDataList.add(0, new LabelValueBean("", ""));
		} else {
			firstItem.setLabel("");
		}
		return staticDataList;
	}

	/**
	 * Refresh the lock on the submission case. This lock must correspond to the
	 * lock acquired in the View Census Details page.
	 * 
	 * @param request
	 * @param submissionId
	 * @param contractId
	 * @param submissionCaseTypeCode
	 * @return
	 */
	private String acquireLockOrErrorForward( HttpServletRequest request,
			CensusErrorCorrectionForm form) {
		// refresh the lock
		String userId = String.valueOf(getUserProfile(request).getPrincipal()
				.getProfileId());
		boolean locked = LockManager.getInstance(request.getSession(false))
				.lock(form.getSubmissionId(), form
						.getContractId(), form.getSubmissionCaseTypeCode(),
						userId);
		
		if (! locked) {
			Collection<ValidationError> lockError = new ArrayList<ValidationError>(
					1);
			lockError.add(new ValidationError("LOCKED",
					ErrorCodes.SUBMISSION_CASE_LOCKED));
			setErrorsInSession(request, lockError);
			return forwards.get(Forward.LOCK_ERROR);
		}
		
		return null;
	}

	private List<Pair<Property, EmployeeValidationError>> getSpecialError(
			CensusErrorCorrectionForm form) {

		Map<EmployeeValidationErrorCode, List<Pair<Property, EmployeeValidationError>>> specialErrors = 
			new HashMap<EmployeeValidationErrorCode, List<Pair<Property, EmployeeValidationError>>>();

		EmployeeValidationErrors errors = form.getErrors();

		/*
		 * First, we need to handle record level errors.
		 */
		for (EmployeeValidationErrorCode specialErrorCode : RECORD_LEVEL_SPECIAL_HANDLING_ERROR_CODES) {
			List<EmployeeValidationError> recordErrors = errors
					.getRecordErrors();
			for (EmployeeValidationError recordError : recordErrors) {
				if (specialErrorCode.equals(recordError.getErrorCode())) {
					/*
					 * For record error, we set the property field to null.
					 */
					addToSpecialErrorList(specialErrors, null,
							specialErrorCode, recordError);
				}
			}
		}

		/*
		 * If we don't have record level errors, we can move onto field level
		 * errors.
		 */
		if (specialErrors.size() == 0) {
			for (EmployeeValidationErrorCode specialErrorCode : FIELD_LEVEL_SPECIAL_HANDLING_ERROR_CODES) {
				for (Property property : EmployeeData.PROPERTY_EDITORS.keySet()) {
					EmployeeValidationError specialError = CensusErrorCorrectionErrorUtil
							.getInstance().getEmployeeValidationError(errors,
									property, specialErrorCode);
					if (specialError != null) {
						if (specialErrorCode
								.equals(EmployeeValidationErrorCode.SimilarSSN)
								&& form.isSimilarSsnAccepted()) {
							/*
							 * If similar SSN is accepted, we can continue.
							 */
							continue;
						}
						addToSpecialErrorList(specialErrors, property,
								specialErrorCode, specialError);
					}
				}
			}
		}

		/*
		 * Pick the first one if there is any. No requirement on how to handle
		 * if multiple special errors are present.
		 */
		if (specialErrors.size() > 0) {
			for (EmployeeValidationErrorCode specialErrorCode : RECORD_LEVEL_SPECIAL_HANDLING_ERROR_CODES) {
				if (specialErrors.containsKey(specialErrorCode)) {
					return specialErrors.get(specialErrorCode);
				}
			}
			for (EmployeeValidationErrorCode specialErrorCode : FIELD_LEVEL_SPECIAL_HANDLING_ERROR_CODES) {
				if (specialErrors.containsKey(specialErrorCode)) {
					return specialErrors.get(specialErrorCode);
				}
			}
		}

		return null;
	}

	private void addToSpecialErrorList(
			Map<EmployeeValidationErrorCode, List<Pair<Property, EmployeeValidationError>>> specialErrors,
			Property property, EmployeeValidationErrorCode specialErrorCode,
			EmployeeValidationError specialError) {

		List<Pair<Property, EmployeeValidationError>> errorList = specialErrors
				.get(specialErrorCode);
		if (errorList == null) {
			errorList = new ArrayList<Pair<Property, EmployeeValidationError>>();
			specialErrors.put(specialErrorCode, errorList);
		}
		errorList.add(new Pair<Property, EmployeeValidationError>(property,
				specialError));
	}
	
	/**
	 * This code has been changed and added to Validate form and request against
	 * penetration attack, prior to other validations.
	 */
	@Autowired
	private PSValidatorFWInput psValidatorFWInput;

	@InitBinder
	public void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWInput);
	}
	
}