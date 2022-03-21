package com.manulife.pension.ps.web.census;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.census.util.EmployeeSnapshotSecurityProfile;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWTpaEmployee;
import com.manulife.pension.ps.web.withdrawal.WebConstants;
import com.manulife.pension.service.employee.valueobject.Employee;
import com.manulife.pension.service.employee.valueobject.EmployeeVestingInfo;
import com.manulife.pension.service.employee.valueobject.EmployeeVestingInfo.VestingType;
/**
 * View Employee snapshot action from TPA site
 * 
 * @author guweigu
 *
 */
@Controller
@RequestMapping(value="/census")

public class TPAViewEmployeeSnapshotController extends EmployeeSnapshotController {

	@ModelAttribute("employeeForm") 
	public EmployeeSnapshotForm populateForm()
	{
		return new EmployeeSnapshotForm();
	}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	
	static{ 
		forwards.put("viewEmployeeSnapshot","/census/tpaViewEmployeeSnapshot.jsp");
		forwards.put("systemErrorPage","/error.jsp");
	}

	
	public TPAViewEmployeeSnapshotController() {
		super(TPAViewEmployeeSnapshotController.class);
	}
	
	
		protected String doCommon( EmployeeSnapshotForm actionForm, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, SystemException {		
			
		HttpSession session = request.getSession(false);		
		Integer contractNumber = (Integer) session.getAttribute(Constants.TPA_CONTRACT_ID_KEY);
				
				String contractName = (String) session.getAttribute(Constants.TPA_CONTRACT_NAME_KEY);
				
				if (contractNumber == null ) {
					logger.error("No TPA_WITHDRAWAL_CONTRACT_ID session attribute is set");
					
					return forwards.get(SYSTEM_ERROR_PAGE);
				}
				
				request.setAttribute(Constants.TPA_CONTRACT_ID_KEY, contractNumber);
				request.setAttribute(Constants.TPA_CONTRACT_NAME_KEY, contractName);
				
				try {			
					long profileId = Long.parseLong(actionForm.getProfileId());

					setVestingSection(contractNumber,actionForm
							);
					
					UserProfile userProfile = SessionHelper.getUserProfile(request);

					EmployeeSnapshotSecurityProfile securityProfile = new EmployeeSnapshotSecurityProfile(
							userProfile);

					request.setAttribute("securityProfile", securityProfile);
					
					Employee employee = serviceFacade.getEmployee(profileId,
							contractNumber, new Date(), actionForm.isShowVesting());
					if (!isAccessibleForEmployee(employee, userProfile)) {
						
						return  Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
					}
					
		            checkPartialParticipantStatus(employee, actionForm);
					
		            actionForm.setViewSalary(securityProfile.isViewSalary(employee
							.getEmployeeDetailVO().getMaskSensitiveInfoInd()));
					request.setAttribute(CensusConstants.EMPLOYEE_ATTRIBUTE, getEmployeeForUI(employee));
		            
					// get employment status history
					// Must use the contract number from the session key attribute - as the TPA might not
					// have picked a 'current contract', so we can't use "userProfile.getCurrentContract()"
		            EmployeeVestingInfo statusParamInfo = serviceFacade.getEmployeeVestingInfo(new Long(profileId), 
		                        contractNumber, 
		                        new Date(), VestingType.EMPLOYMENT_STATUS, true, true, false);
		            EmployeeVestingInfo filteredStatusInfo = filterOutCanceledStatus(userProfile, statusParamInfo);
		            request.setAttribute(CensusConstants.EMPLOYEE_STATUS_HISTORY_ATTRIBUTE, filteredStatusInfo);
		            
		            // get employee change history
		            List historyList = serviceFacade.getEmployeeChangeHistory(profileId, contractNumber);
		            EmployeeHistoryView history = new EmployeeHistoryView(historyList);
		            
		            // update previous values from the employee_vesting_parameter table
		            updatePreviousStatusFromParamTable(history, filteredStatusInfo);
		            request.setAttribute(CensusConstants.EMPLOYEE_HISTORY_ATTRIBUTE, history);
		            
					//validateEmployee( f, request, employee);
				} catch (SystemException e) {
					logger.error("Fail to get Employee information: ", e);
					throw e;
				} catch (NumberFormatException e) {
					logger.error(
							"ProfileId or ContractId is not in correct format: profileId = "
									+ actionForm.getProfileId() + " contractId = " + contractNumber, e);
					
					return forwards.get(SYSTEM_ERROR_PAGE);
				}

				
				return forwards.get("viewEmployeeSnapshot");
			}	
				
		
	@RequestMapping(value="/tpaViewEmployeeSnapshot/",  method =  {RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("employeeForm") EmployeeSnapshotForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
		    throws IOException,ServletException, SystemException {
		String  forward= preExecute(actionForm, request, response);
        if ( StringUtils.isNotBlank(forward)) {
     	    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("viewEmployeeSnapshot");
	       }
		}
		 forward = doCommon(actionForm, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}

    /**
     * {@inheritDoc}
     */
    @Override
    protected String preExecute( final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException, SystemException {
        // Get the existing value.
        final Object lastPage = request.getSession().getAttribute(
                WebConstants.LAST_ACTIVE_PAGE_LOCATION);

        // This operation resets/clears the last active page location.
        final String forwardResult = super.preExecute( form, request, response);

        // Put the previously the existing value back.
        request.getSession().setAttribute(WebConstants.LAST_ACTIVE_PAGE_LOCATION, lastPage);

        return forwardResult;
    }
    
	/* (non-Javadoc)
	 * Over ridden method to avoid token been rest from withdrawal pages.
	 * 
	 * @see com.manulife.pension.platform.web.controller.BaseAction#isTokenRequired(java.lang.String)
	 */
    protected boolean isTokenRequired(String action) {
		return false;
	}
    
    /**
	 * This code has been changed and added to Validate form and request against
	 * penetration attack, prior to other validations.
	 */
	
	 @Autowired
	   private PSValidatorFWTpaEmployee  psValidatorFWTpaEmployee;

	 @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWTpaEmployee);
	}



	
    
}
