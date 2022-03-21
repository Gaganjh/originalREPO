package com.manulife.pension.ps.web.census;

import static com.manulife.pension.service.contract.valueobject.WithdrawalReason.DEATH;
import static com.manulife.pension.service.contract.valueobject.WithdrawalReason.DISABILITY;
import static com.manulife.pension.service.contract.valueobject.WithdrawalReason.PRE_RETIREMENT;
import static com.manulife.pension.service.contract.valueobject.WithdrawalReason.RETIREMENT;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.EligibilityServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.census.util.EmployeeSnapshotErrorUtil;
import com.manulife.pension.ps.web.census.util.EmployeeSnapshotSecurityProfile;
import com.manulife.pension.ps.web.census.util.ParameterizedActionForward;
import com.manulife.pension.ps.web.contract.csf.EligibilityCalculationMoneyType;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.ps.web.withdrawal.WebConstants;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.eligibility.valueobject.EmployeePlanEntryVO;
import com.manulife.pension.service.employee.valueobject.Employee;
import com.manulife.pension.service.employee.valueobject.EmployeeVestingInfo;
import com.manulife.pension.service.employee.valueobject.EmployeeVestingInfo.VestingType;
import com.manulife.pension.service.employee.valueobject.EmployeeVestingVO;
import com.manulife.pension.service.plan.valueobject.PlanDataLite;
import com.manulife.pension.service.security.role.ExternalUser;
import com.manulife.pension.service.security.valueobject.UserInfo;

/**
 * Participant Menu Action class This class is used to forward the users's
 * request to the Participant Menu page.
 * 
 * @author celarro
 */
@Controller
@RequestMapping(value = "/census")

public final class ViewEmployeeSnapshotController extends EmployeeSnapshotController {
	
	@ModelAttribute("employeeForm") 
	public EmployeeSnapshotForm populateForm() 
	{
		return new EmployeeSnapshotForm();
	}
	
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	
	static{
		forwards.put("input","/census/viewEmployeeSnapshot.jsp"); 
		forwards.put("viewEmployeeSnapshot","/census/viewEmployeeSnapshot.jsp");
		forwards.put("censusSummary","redirect:/do/census/censusSummary/"); 
		forwards.put("editEmployeeSnapshot","redirect:/do/census/editEmployeeSnapshot/");
		forwards.put("home path","redirect:/do/home/homePageFinder/");
		forwards.put("addressHistory","redirect:/do/participant/addressHistory");
		forwards.put("eligibilitySummary","redirect:/do/census/employeeEnrollmentSummary"); 
		forwards.put("addressSummary","redirect:/do/participant/participantAddresses");
		forwards.put("participantAccount","redirect:/do/participant/participantAccount/");
		forwards.put("taskCenter","redirect:/do/participant/taskCenterTasks");
		forwards.put("deferral","redirect:/do/census/deferral"); 
		forwards.put("censusVesting","redirect:/do/census/censusVesting/"); 
		forwards.put("vestingInformation","redirect:/do/census/viewVestingInformation/"); 
		forwards.put("eligibilityInformation","redirect:/do/census/eligibilityInformation/");
		forwards.put("resetPassword","redirect:/do/pwdemail/ResetPassword/");
		forwards.put("payrollSelfService","redirect:/do/participant/payrollSelfService/");
		forwards.put("challengePasscode", "redirect:/do/passcodeTransition/");
		}

	/**
	 * Default Constructor.
	 * 
	 */
	public ViewEmployeeSnapshotController() {
		super(ViewEmployeeSnapshotController.class);
	}
	
	@RequestMapping(value ="/viewEmployeeSnapshot/" , params = {"action=back"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doBack(@Valid @ModelAttribute("employeeForm") EmployeeSnapshotForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		/*String forward = preExecute(actionForm, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
		}*/
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		EmployeeSnapshotForm f = (EmployeeSnapshotForm) actionForm;

		
		
		String forward =forwardToRealSource(request,actionForm);
        return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		
	}
	@RequestMapping(value ="/viewEmployeeSnapshot/",params={"action=showPreviousValue"},  method ={RequestMethod.POST})
    public String doShowPreviousValue(
    		@Valid @ModelAttribute("employeeForm") EmployeeSnapshotForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
	 * Got to Edit employee snapshot page
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
	@RequestMapping(value ="/viewEmployeeSnapshot/" , params = {"action=edit"},method =  {RequestMethod.POST}) 
	public String doEdit(@Valid @ModelAttribute("employeeForm") EmployeeSnapshotForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward = preExecute(actionForm, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
		}
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		
		UserProfile userProfile = getUserProfile(request);
		UserInfo userInfo = SecurityServiceDelegate.getInstance().getUserInfo(userProfile.getPrincipal());
		EmployeeSnapshotForm employeeSnapshotForm = (EmployeeSnapshotForm) actionForm;
		String profileId = request.getParameter("profileId");
		Employee employeeSnapsot = serviceFacade.getEmployee(Long.parseLong(employeeSnapshotForm.getProfileId()),
				userProfile, new Date(), employeeSnapshotForm.isShowVesting());
		String employeeSSN = employeeSnapsot.getEmployeeDetailVO().getSocialSecurityNumber();
		 boolean isExternalUser = userProfile.getRole() instanceof ExternalUser;
		 request.getSession().setAttribute("challengeRequestFrom", "viewEmployeeSnapshotPage");
		if (isExternalUser && userInfo.getSsn().equals(employeeSSN) && null != request.getSession(false).getAttribute(Constants.CHALLENGE_PASSCODE_IND)) {
				boolean isChallengedAlready = (Boolean) request.getSession(false)
						.getAttribute(Constants.CHALLENGE_PASSCODE_IND);
				
				if (isChallengedAlready) {
					//request.getSession(false).setAttribute("challengeRequestFrom", "viewEmployeeSnapshotPage");
					request.getSession(false).setAttribute("employeeProfileId",
							Long.parseLong(employeeSnapshotForm.getProfileId()));
					return forwards.get("challengePasscode");
				}
		}
		String f = forwards.get("editEmployeeSnapshot");
		ParameterizedActionForward editForward = new ParameterizedActionForward(f);
		editForward.addParameter("profileId", profileId);
		if (actionForm.getSource() != null) {
			editForward.addParameter("source", actionForm.getSource());
		}
		editForward.addParameter("fromView", "true");
		editForward.addParameter("expandBasic", Boolean.toString(actionForm
				.isExpandBasic()));
		editForward.addParameter("expandEmployment", Boolean.toString(actionForm
				.isExpandEmployment()));
		editForward.addParameter("expandContact", Boolean.toString(actionForm
				.isExpandContact()));
		editForward.addParameter("expandParticipation", Boolean.toString(actionForm
				.isExpandParticipation()));
		editForward.addParameter("expandVesting", Boolean.toString(actionForm
				.isExpandVesting()));
		editForward.addParameter("showConfirmationToDo", "false");

		return editForward.getPath();
	}
	
	@RequestMapping(value ="/viewEmployeeSnapshot/",  method =  {RequestMethod.GET}) 
	protected String doDefault (@Valid @ModelAttribute("employeeForm") EmployeeSnapshotForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward = preExecute(actionForm, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
		}
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		if (logger.isDebugEnabled()) {
			logger.debug(ViewEmployeeSnapshotController.class.getName()
					+ ":forwarding to Employee Snapshot - View.");
		}


		UserProfile userProfile = SessionHelper.getUserProfile(request);		
		setVestingSection(userProfile.getCurrentContract().getContractNumber(),
				actionForm);
		
		//Set OBDS link to display or not
		actionForm.setOnlineBeneficiaryLinkDisplayed(actionForm
				.displayBeneficiaryLinkChoose(userProfile.getCurrentContract()
						.getContractNumber()));
		// initialize the form for expansion etc..
		initializeSectionExpansion(actionForm);
		if(!displayFlag){
		actionForm.setExpandBasic(true);
		actionForm.setExpandContact(false);
		actionForm.setExpandEmployment(false);
		actionForm.setExpandParticipation(false);
		actionForm.setExpandVesting(false);
		}
		forward = doCommon(actionForm, request, response);
		return forwards.get(forward);

	}

	protected String doCommon(EmployeeSnapshotForm actionForm, HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
		
		int contractNumber = 0;
		UserProfile userProfile = SessionHelper.getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();
		EmployeeSnapshotSecurityProfile securityProfile = new EmployeeSnapshotSecurityProfile(
				userProfile);

		request.setAttribute("securityProfile", securityProfile);
		if (actionForm.getProfileId() == null) {
			logger.error("No profileId is passed in: ");
			
			return SYSTEM_ERROR_PAGE;
		}
		long profileId = Long.parseLong(actionForm.getProfileId());

		try {
			Employee employee = serviceFacade.getEmployee(profileId,
					userProfile, new Date(), actionForm.isShowVesting());

	        checkPartialParticipantStatus(employee, actionForm);
	        
			//Check if EC csf is on for this contract.
	        setParticipationSection(userProfile.getCurrentContract().getContractNumber(),employee,actionForm,currentContract);
			
	        if (actionForm.isShowVesting()) {
				VestingInformation vestingInformation = getVestingInformation(actionForm, employee);
				request.getSession(false).setAttribute(CensusConstants.VESTING_INFO, vestingInformation);
                vestingInformation.setPartialParticipantStatus(actionForm.getPartialParticipantStatus());
			}

			if (!isAccessibleForEmployee(employee, userProfile)) {
				//return redirectForNotAccessible(mapping);
			}
			
			actionForm.setViewSalary(securityProfile.isViewSalary(employee
					.getEmployeeDetailVO().getMaskSensitiveInfoInd()));
			request.setAttribute(CensusConstants.EMPLOYEE_ATTRIBUTE, getEmployeeForUI(employee));
            
            // get employment status history
            EmployeeVestingInfo statusParamInfo = serviceFacade.getEmployeeVestingInfo(new Long(profileId), 
                        userProfile.getCurrentContract().getContractNumber(), 
                        new Date(), VestingType.EMPLOYMENT_STATUS, true, true, false);
            EmployeeVestingInfo filteredStatusInfo = filterOutCanceledStatus(userProfile, statusParamInfo);
            request.setAttribute(CensusConstants.EMPLOYEE_STATUS_HISTORY_ATTRIBUTE, filteredStatusInfo);
            
            // get employee change history
			List historyList = serviceFacade.getEmployeeChangeHistory(profileId, userProfile);
			EmployeeHistoryView history = new EmployeeHistoryView(historyList);
			
			// get plan entry data & YTD Hours,effective date
			Map data = getPlanYTDHoursData(statusParamInfo);
            request.setAttribute(CensusConstants.EMPLOYEE_PLAN_HOURS_WORKED_HISTORY, data);
            
			//updates the history of eligibility date by ECservice money type.
            updateEligibilityDateHistory(historyList,history,actionForm);
            // update previous values from the employee_vesting_parameter table
            updatePreviousStatusFromParamTable(history, filteredStatusInfo);
            request.setAttribute(CensusConstants.EMPLOYEE_HISTORY_ATTRIBUTE, history);
            
            if(actionForm.isEligibilityCalcCsfOn()) {
                if (employee.getEmployeePlanEntryVoList() == null
        				|| employee.getEmployeePlanEntryVoList().isEmpty()) {
        		    ArrayList eligibleEmployeePlanEntryVoList=new ArrayList();
        			int totalNumberOfMoneyTypes = actionForm
        					.getEligibilityServiceMoneyTypes().size();
        			for (int i = 0; i < totalNumberOfMoneyTypes; i++) {
        				EligibilityCalculationMoneyType currentECalcMoneyType = actionForm
        						.getEligibilityServiceMoneyTypes().get(i);
        				EmployeePlanEntryVO employeePlanEntryVo = getEmployeeplanEntryVo(
        						request, employee, actionForm, currentECalcMoneyType
        								.getMoneyTypeId());
        				eligibleEmployeePlanEntryVoList.add(employeePlanEntryVo);
        			}
        			employee.setEligibleEmployeePlanEntryVoList(eligibleEmployeePlanEntryVoList);
        			EmployeeSnapshotErrorUtil.getInstance().setChangedSet(actionForm.getEligibilityMoneyTypeFieldNames());
        		}
                }
            
			validateEmployee(actionForm, request, employee);
			actionForm.setAe90DaysOptOutDisplayed(actionForm.displayAe90DaysOptOut(employee.getEmployeeDetailVO().getContractId()));
		} catch (SystemException e) {
			logger.error("Fail to get Employee information: ", e);
			throw e;
		} catch (NumberFormatException e) {
			logger.error(
					"ProfileId or ContractId is not in correct format: profileId = "
							+ profileId + " contractId = " + contractNumber, e);
			
			return forwards.get(SYSTEM_ERROR_PAGE);
		}
		
		String forward = VIEW_EMPLOYEE_SNAPSHOT_PAGE;
		return forward;
	}
    
	private VestingInformation getVestingInformation(
			EmployeeSnapshotForm form,
			Employee employee) throws SystemException {
		
		VestingInformation vi = new VestingInformation();
        //final PlanData planData = ContractServiceDelegate.getInstance().readPlanData(
               // employee.getEmployeeDetailVO().getContractId(), true);
		final PlanDataLite planDataLite = ContractServiceDelegate.getInstance()
				.getPlanDataLight(
						employee.getEmployeeDetailVO().getContractId());
        vi.setFullyVestedOnDeath(planDataLite.getWithdrawalReasons().contains(DEATH));
        vi.setFullyVestedOnRetirement(planDataLite.getWithdrawalReasons().contains(RETIREMENT));
        vi.setFullyVestedOnEarlyRetirement(planDataLite.getWithdrawalReasons().contains(PRE_RETIREMENT));
        vi.setFullyVestedOnDisability(planDataLite.getWithdrawalReasons().contains(DISABILITY));
        vi.setEmployeeFirstName(employee.getEmployeeDetailVO().getFirstName());
		vi.setEmployeeLastName(employee.getEmployeeDetailVO().getLastName());
		vi.setEmployeeMiddleInit(employee.getEmployeeDetailVO().getMiddleInitial());
		vi.setEmployeeSSN(employee.getEmployeeDetailVO().getSocialSecurityNumber());
        
		final EmployeeVestingVO employeeVestingVO = employee.getEmployeeVestingVO();
		vi.setEmployeeVestingInformation(employeeVestingVO.getEmployeeVestingInformation());
		vi.setMoneyTypes(form.getMoneyTypes());

        vi.setPlanHoursOfService(planDataLite.getHoursOfService());

		return vi;
	}
	
	/**
     * {@inheritDoc}
     */
	protected String preExecute (EmployeeSnapshotForm actionForm, HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
   
        // Get the existing value.
        final Object lastPage = request.getSession().getAttribute(
                WebConstants.LAST_ACTIVE_PAGE_LOCATION);

        // This operation resets/clears the last active page location.
        final String forwardResult = super.preExecute( actionForm, request, response);

        // Put the previously the existing value back.
        request.getSession().setAttribute(WebConstants.LAST_ACTIVE_PAGE_LOCATION, lastPage);

        return forwardResult;
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
    			EmployeeSnapshotForm  form, String currentECalcMoneyType)
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
    	
	/*
	 * avoids token generation as this class acts as intermediate for many
	 * transactions(non-Javadoc)
	 * 
	 * @see
	 * com.manulife.pension.platform.web.controller.BaseAction#isTokenRequired
	 * (java.lang.String)
	 */
	protected boolean isTokenRequired(String action) {
		return true;
	}

	/*
	 * Returns true if token has to be validated for the particular action call
	 * to avoid CSRF vulnerability else false. (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ezk.web.BaseAction#isTokenValidatorEnabled(java
	 * .lang.String)
	 */
	@Override
	public boolean isTokenValidatorEnabled(String actionName) {
		// avoids methods from validation which ever is not required
		/*if (StringUtils.isNotEmpty(actionName)
				&& (StringUtils.contains(actionName, "Default") || StringUtils
						.contains(actionName, "default") || 
						(StringUtils.contains(actionName, "ShowPreviousValue")))) {
			return false;
		} else {
			return true;
		}*/
		return StringUtils.isNotEmpty(actionName)
				&& (StringUtils.equalsIgnoreCase(actionName, "Edit"))?true:false;
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
