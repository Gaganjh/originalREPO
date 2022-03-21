package com.manulife.pension.ps.web.census.util;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.delegate.EligibilityServiceDelegate;
import com.manulife.pension.delegate.EmployeeServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.census.CensusConstants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.service.eligibility.valueobject.EmployeePlanEntryVO;
import com.manulife.pension.service.employee.EmployeeValidationException;
import com.manulife.pension.service.employee.util.EmployeeValidationErrors;
import com.manulife.pension.service.employee.valueobject.AddressVO;
import com.manulife.pension.service.employee.valueobject.Employee;
import com.manulife.pension.service.employee.valueobject.EmployeeChangeHistoryVO;
import com.manulife.pension.service.employee.valueobject.EmployeeDetailVO;
import com.manulife.pension.service.employee.valueobject.EmployeeVestingInfo;
import com.manulife.pension.service.employee.valueobject.EmployeeVestingVO;
import com.manulife.pension.service.employee.valueobject.UserIdType;
import com.manulife.pension.service.employee.valueobject.EmployeeVestingInfo.VestingType;
import com.manulife.pension.service.security.Principal;

/**
 * Provide a facade to employee service from the EmployeeSnapshot.
 * 
 * @author guweigu
 * 
 */
public class EmployeeServiceFacade {

    /**
     * Get Employee value object.
     * 
     * @param profileId
     * @param contractId
     * @return
     * @throws SystemException
     */
    public Employee getEmployee(long profileId, int contractId, Date asOfDate, boolean withVesting) throws SystemException {
    	if (withVesting) {
    		return getEmployeeService().getEmployeeAndVestingByProfileId(profileId, contractId, asOfDate);
    	} else {
    		return getEmployeeService().getEmployeeByProfileId(profileId, contractId, asOfDate);
    	}
    }
    
    /** 
     * Get vesting param info for a given employee
     * 
     * @param profileId
     * @param contractId
     * @param asOfDate
     * @param vestingType
     * @param retrieveNonCurrent
     */
    public EmployeeVestingInfo getEmployeeVestingInfo(Long profileId,
            Integer contractId, Date asOfDate, VestingType vestingType,
            boolean retrieveNonCurrent, boolean retrieveCurrentVYOSOnly,
            boolean returnEmptyFullyVestedIfNotFoundInDB) throws SystemException  {
        return getEmployeeService().getEmployeeVestingInfo(profileId, contractId, 
                asOfDate, vestingType, retrieveNonCurrent, retrieveCurrentVYOSOnly,
                returnEmptyFullyVestedIfNotFoundInDB);
    }

    /**
     * Get Employee value object for the profileId and the current contract.
     * 
     * @param profileId
     * @param user
     * @return
     * @throws SystemException
     */
    public Employee getEmployee(long profileId, UserProfile user, Date asOfDate, boolean withVesting) throws SystemException {
        return getEmployee(profileId, user.getCurrentContract().getContractNumber(), asOfDate, withVesting);
    }

    public List<EmployeeChangeHistoryVO> getEmployeeChangeHistory(long profileId, int contractId)
            throws SystemException {
        return getEmployeeService().getEmployeeChangeHistoryCurrentAndPrevious(profileId,
                contractId);
    }

    public List<EmployeeChangeHistoryVO> getEmployeeChangeHistory(long profileId, UserProfile user)
            throws SystemException {
        return getEmployeeChangeHistory(profileId, user.getCurrentContract().getContractNumber());
    }


    public Employee updateEmployee(Employee employee, UserProfile user, long updaterProfileId, boolean ignorePYOS, boolean ignoreFullyVested)

            throws EmployeeValidationException, SystemException {
        return updateEmployee(employee, user, false, false, ignorePYOS, ignoreFullyVested);
    }

    /**
     * Update Employee. Before calling employee service, it sets the necessary information in the
     * Employee object.
     * 
     * @param employee
     * @param user
     * @return
     * @throws SystemException
     */
    public Employee updateEmployee(Employee employee, UserProfile user, boolean ignoreWarning,
            boolean ignoreSimilarSsn, boolean ignorePYOS, boolean ignoreFullyVested) throws EmployeeValidationException, SystemException {
    	boolean isNewEmployee = false;
    	// Check whether its a new employee
        if (employee.getEmployeeDetailVO().getProfileId() == null) {
        	isNewEmployee = true;
		}
        
        // set confirm indicator
        employee.setConfirmedIndicator("Y");
        EmployeeDetailVO detail = employee.getEmployeeDetailVO();

        // check the comment length
        String comments = detail.getMaskSensitiveInfoComments();
        if (!StringUtils.isEmpty(comments)
                && comments.length() > CensusConstants.MaxLengthOfMaskSensitiveInfoComment) {
            detail.setMaskSensitiveInfoComments(comments.substring(0,
                    CensusConstants.MaxLengthOfMaskSensitiveInfoComment));
        }
        detail.setContractId(new Integer(user.getCurrentContract().getContractNumber()));
        // set up the created timestamp and created user id if it is null
        Principal principal = user.getPrincipal();

        // set up the last updated user profile id
        employee.setUserId(Long.toString(principal.getProfileId()));
        employee.setUserIdType(user.isInternalUser() ? UserIdType.UP_INTERNAL
                : UserIdType.UP_EXTERNAL);
        detail.setSourceChannelCode(Constants.PS_APPLICATION_ID);

        // set following fields to null if they are blank
        detail.setNamePrefix(StringUtils.trimToNull(detail.getNamePrefix()));
        detail.setMiddleInitial(StringUtils.trimToNull(detail.getMiddleInitial()));
        detail.setEmployerProvidedEmailAddress(StringUtils.trimToNull(detail
                .getEmployerProvidedEmailAddress()));
        detail.setEmployeeId(StringUtils.trimToNull(detail.getEmployeeId()));
        detail.setEmployerDivision(StringUtils.trimToNull(detail.getEmployerDivision()));
        detail.setResidenceStateCode(StringUtils.trimToNull(detail.getResidenceStateCode()));
        detail.setMaskSensitiveInfoComments(StringUtils.trimToNull(detail
                .getMaskSensitiveInfoComments()));
        detail.setAutoEnrollOptOutInd(StringUtils.trimToNull(detail.getAutoEnrollOptOutInd()));
        
        AddressVO address = employee.getAddressVO();
        // need to set the updated information for address
        if (address != null) {
            address.setLastUpdatedByFirstName(principal.getFirstName());
            address.setLastUpdatedByLastName(principal.getLastName());
            address.setLastUpdatedByUserId(principal.getUserName());
            address.setLastUpdatedByUserIdType(user.isInternalUser() ? UserIdType.UP_INTERNAL
                    : UserIdType.UP_EXTERNAL);
        }

        // blank string is not a valid value
        if (StringUtils.isEmpty(detail.getEmploymentStatusCode())) {
            detail.setEmploymentStatusCode(null);
        }

        if (detail.getUnionMemberIndicator() == null) {
            detail.setUnionMemberIndicator("N");
        }

        if (detail.getNonResidentAlienInd() == null) {
            detail.setNonResidentAlienInd("N");
        }

        EmployeeVestingVO vesting = employee.getEmployeeVestingVO();
        vesting.setPlanEligibleInd(StringUtils.trimToNull(vesting.getPlanEligibleInd()));
        
        if (StringUtils.isEmpty(vesting.getFullyVestedInd())) {
            vesting.setFullyVestedInd("");
        }

        if (vesting.getServiceCreditedThisYearInd() == null) {
            vesting.setServiceCreditedThisYearInd("N");
        }
        
		Employee updatedEmployee = getEmployeeService()
				.validateAndUpdateEmployee(employee, ignoreWarning,
						ignoreSimilarSsn, user.getPrincipal().getProfileId(),
						ignorePYOS, ignoreFullyVested,
						user.isInternalUser() ? "UPI" : "UPE");
		
		 // Trigger the eligibility calculation
        EligibilityServiceDelegate delegate = EligibilityServiceDelegate.getInstance(GlobalConstants.PSW_APPLICATION_ID);
        delegate.invokeEligibilityOrPlanEntryCalculation(updatedEmployee, isNewEmployee);
        
        return updatedEmployee;

    }

    public EmployeeValidationErrors validate(Employee employee, UserProfile user,
            boolean ignoreSimilarSsn, boolean ignorePYOS, boolean ignoreFullyVested) throws SystemException {
        EmployeeDetailVO detail = employee.getEmployeeDetailVO();
        detail.setContractId(new Integer(user.getCurrentContract().getContractNumber()));
        return getEmployeeService().validate(employee, ignoreSimilarSsn, ignorePYOS, ignoreFullyVested);
    }

    private EmployeeServiceDelegate getEmployeeService() {
        return EmployeeServiceDelegate.getInstance(Constants.PS_APPLICATION_ID);
    }
    
    /** 
     * Update vesting param info for a given employee
     * 
     * @param vestingInfo
     */
    public void updateEmployeeVestingInfo(EmployeeVestingInfo vestingInfo) throws SystemException {
        getEmployeeService().updateEmployeeVestingInfo(vestingInfo);
    }
    
    /**
     * Update the employeeId for a given employee
     * 
     * @param profileId The profile id of the employee to update
     * @param contractId The contract id of the employee to update
     * @param employeeId The employee id value to set
     * @throws SystemException
     */
    public void updateEmployeeId(Long profileId, Integer contractId, String employeeId,
            long userProfileId, String userTypeCode, String sourceChannelCode) throws SystemException {
        getEmployeeService().updateEmployeeId(profileId, contractId, employeeId, userProfileId, userTypeCode, sourceChannelCode);
    }
    
    /**
	 * Updates the existing CSDB employer provided email id with
	 * newEmployerProvidedEmailAddress in EMPLOYEE_CONTRACT and creates an entry
	 * in History tables and updates the EMAIL_ADDR_END_TS as current timestamp
	 * in EMPLOYEE_EMAIL_ADDRESS table.
	 * 
	 * @param profileId
	 * @param contractId
	 * @param employerProvidedEmailAddress
	 * @param sourceChannelCode
	 * @param confirmedIndicator
	 * @param userId
	 * @param userTypeCode
	 * @throws SystemException
	 */

	public void updateEmployerProvidedEmailAddress(Long profileId,
			Integer contractId, String employerProvidedEmailAddress,
			String sourceChannelCode, String confirmedIndicator, String userId,
			String userTypeCode) throws SystemException {

		getEmployeeService().updateEmployerProvidedEmailAddress(profileId,
				contractId, employerProvidedEmailAddress, sourceChannelCode,
				confirmedIndicator, userId, userTypeCode);
	}
	
    /**
	 * Create or Update Long Term Part Time Assessment Employee Plan Entry Details
	 * 
	 * @param employeePlanEntryVO
	 * @param userId
	 * @param userIdType
	 * @param psSourceChannelCode
	 * @param psSourceFunctionCodeEes
	 * @throws SystemException
	 */
	public void createOrUpdateLongTermPartTimeAssessmentEmployeePlanEntryDetail(EmployeePlanEntryVO employeePlanEntryVO,
			String userId, String userIdType, String psSourceChannelCode, String psSourceFunctionCodeEes)
			throws SystemException {

		getEmployeeService().createOrUpdateLongTermPartTimeAssessmentEmployeePlanEntryDetail(employeePlanEntryVO,
				userId, userIdType, psSourceChannelCode, psSourceFunctionCodeEes);
	}

}
