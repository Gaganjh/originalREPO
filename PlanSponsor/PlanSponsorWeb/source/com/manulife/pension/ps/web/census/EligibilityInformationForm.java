package com.manulife.pension.ps.web.census;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.service.contract.valueobject.MoneyTypeVO;
import com.manulife.pension.service.eligibility.valueobject.EligibilityRequestVO;
import com.manulife.pension.service.eligibility.valueobject.EmployeePlanEntryVO;
import com.manulife.pension.service.eligibility.valueobject.PlanEntryRequirementDetailsVO;
import com.manulife.pension.service.employee.valueobject.Employee;
import com.manulife.pension.service.environment.valueobject.LabelValueBean;

/**
 * Form Bean for eligibility information page
 * 
 * @author Siby Thomas
 * 
 */
public class EligibilityInformationForm extends AutoForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int profileId;
	private Date latestInformationDate;
	private Collection<EligibilityRequestVO> pendingRequestList;
	private boolean pendingEligibilityCalculationRequest;
	private Collection<MoneyTypeVO> moneyTypeDescList;
	private Collection<PlanEntryRequirementDetailsVO> planMoneyTypeDetailsList;
	private Collection<EmployeePlanEntryVO> employeeMoneyTypeDetailsList;
	private Map<String, Date> serviceElectionDates;
	private Employee employeeDetails;
	private String employeeAge;
	private Map<String, LabelValueBean> planYtdData;
	private int longTermPartTimeAssessmentYear;
	private boolean displayLongTermPartTimeAssessmentYearField;

	/**
	 * gets the profile ID
	 * 
	 * @return profileId int
	 */
	public int getProfileId() {
		return profileId;
	}

	/**
	 * sets the profile ID
	 * 
	 * @param profileId
	 *            int
	 */
	public void setProfileId(int profileId) {
		this.profileId = profileId;
	}

	/**
	 * gets the latest updated date for money types
	 * 
	 * @return latestInformationDate Date
	 */
	public Date getLatestInformationDate() {
		return latestInformationDate;
	}

	/**
	 * sets the latest updated date for money types
	 * 
	 * @param latestInformationDate
	 *            Date
	 */
	public void setLatestInformationDate(Date latestInformationDate) {
		this.latestInformationDate = latestInformationDate;
	}

	/**
	 * gets the indicator if any pending request is available or not
	 * 
	 * @return pendingEligibilityCalculationRequest boolean
	 */
	public boolean getPendingEligibilityCalculationRequest() {
		return pendingEligibilityCalculationRequest;
	}

	/**
	 * sets the indicator if any pending request is available or not
	 * 
	 * @param pendingEligibilityCalculationRequest
	 *            boolean
	 */
	public void setPendingEligibilityCalculationRequest(
			boolean pendingEligibilityCalculationRequest) {
		this.pendingEligibilityCalculationRequest = pendingEligibilityCalculationRequest;
	}

	/**
	 * gets the list of pending request VO
	 * 
	 * @return pendingRequestList Collection<EligibilityRequestVO>
	 */
	public Collection<EligibilityRequestVO> getPendingRequestList() {
		return pendingRequestList;
	}

	/**
	 * sets the list of pending request VO
	 * 
	 * @param pendingRequestList
	 *            Collection<EligibilityRequestVO>
	 */
	public void setPendingRequestList(
			Collection<EligibilityRequestVO> pendingRequestList) {
		this.pendingRequestList = pendingRequestList;
	}

	public Collection<MoneyTypeVO> getMoneyTypeDescList() {
		return moneyTypeDescList;
	}

	public void setMoneyTypeDescList(Collection<MoneyTypeVO> moneyTypeDescList) {
		this.moneyTypeDescList = moneyTypeDescList;
	}
	
	/**
	 * gets the list of plan money type details VO
	 * 
	 * @return moneyTypeDetails Collection<PlanEntryRequirementDetailsVO>
	 */
	public Collection<PlanEntryRequirementDetailsVO> getPlanMoneyTypeDetailsList() {
		return planMoneyTypeDetailsList;
	}

	/**
	 * sets the list of plan money type details VO
	 * 
	 * @param moneyTypeDetails
	 *            Collection<PlanEntryRequirementDetailsVO>
	 */
	public void setPlanMoneyTypeDetailsList(
			Collection<PlanEntryRequirementDetailsVO> planMoneyTypeDetailsList) {
		this.planMoneyTypeDetailsList = planMoneyTypeDetailsList;
	}
	
	/**
	 * gets the list of employee money type details VO
	 * 
	 * @return moneyTypeDetails Collection<EmployeePlanEntryVO>
	 */
	public Collection<EmployeePlanEntryVO> getEmployeeMoneyTypeDetailsList() {
		return employeeMoneyTypeDetailsList;
	}

	/**
	 * sets the list of employee money type details VO
	 * 
	 * @param moneyTypeDetails
	 *            Collection<EmployeePlanEntryVO>
	 */
	public void setEmployeeMoneyTypeDetailsList(
			Collection<EmployeePlanEntryVO> employeeMoneyTypeDetailsList) {
		this.employeeMoneyTypeDetailsList = employeeMoneyTypeDetailsList;
	}

	/**
	 * get the employee details VO
	 * 
	 * @return employeeDetails Employee
	 */
	public Employee getEmployeeDetails() {
		return employeeDetails;
	}

	/**
	 * sets the employee details VO
	 * 
	 * @param employeeDetails
	 *            Employee
	 */
	public void setEmployeeDetails(Employee employeeDetails) {
		this.employeeDetails = employeeDetails;
	}

	/**
	 * gets the calculated employee age
	 * 
	 * @return employeeAge String
	 */
	public String getEmployeeAge() {
		return employeeAge;
	}

	/**
	 * sets the calculated employee age
	 * 
	 * @param employeeAge
	 *            String
	 */
	public void setEmployeeAge(String employeeAge) {
		this.employeeAge = employeeAge;
	}
	/**
	 * gets the service election dates map
	 * @return Map<String, Date>
	 */
	public Map<String, Date> getServiceElectionDates() {
		return serviceElectionDates;
	}

	/**
	 * sets the service election dates map
	 * @param serviceElectionDates
	 */
	public void setServiceElectionDates(Map<String, Date> serviceElectionDates) {
		this.serviceElectionDates = serviceElectionDates;
	}
	
	/**
	 * gets the plan requirement details vo for the money type
	 * @param moneyTypeId
	 * @return PlanEntryRequirementDetailsVO
	 */
	public PlanEntryRequirementDetailsVO getPlanMoneyDetailsForMoneyId(String moneyTypeId) {
		if(this.planMoneyTypeDetailsList == null) {
			return new PlanEntryRequirementDetailsVO();
		}
		moneyTypeId = StringUtils.trimToEmpty(moneyTypeId);
		for(PlanEntryRequirementDetailsVO vo : this.planMoneyTypeDetailsList){
			if(StringUtils.equals(moneyTypeId, StringUtils.trimToEmpty(vo.getMoneyTypeId()))){
				return vo;
			}
		}
		return new PlanEntryRequirementDetailsVO(); 
	}
	
	/**
	 * gets the employee plan entry vo for money type
	 * @param moneyTypeId
	 * @return EmployeePlanEntryVO
	 */
	public EmployeePlanEntryVO getEmployeeMoneyDetailsForMoneyId(String moneyTypeId) {
		if(this.employeeMoneyTypeDetailsList == null) {
			return new EmployeePlanEntryVO();
		}
		moneyTypeId = StringUtils.trimToEmpty(moneyTypeId);
		for(EmployeePlanEntryVO vo : this.employeeMoneyTypeDetailsList){
			if(StringUtils.equals(moneyTypeId, StringUtils.trimToEmpty(vo.getMoneyTypeId()))){
				return vo;
			}
		}
		return new EmployeePlanEntryVO(); 
	}
	
	/**
	 * gets the service election date for the contract
	 * @param moneyTypeId
	 * @return
	 */
	public Date getServiceElectionDate(String moneyTypeId) {
		if(this.serviceElectionDates == null) {
			return null;
		}
		return this.serviceElectionDates.get(moneyTypeId);
	}
	
	/**
	 * get plan ytd data
	 * @return planYtdData
	 */
	public Map<String, LabelValueBean> getPlanYtdData() {
		return planYtdData;
	}

	/**
	 * return plan ytd data
	 * @param planYtdData
	 */
	public void setPlanYtdData(Map<String, LabelValueBean> planYtdData) {
		this.planYtdData = planYtdData;
	}
	
	/**
	 * get longTermPartTimeAssessmentYear
	 * @return Long Term Part Time Assessment Year
	 */
	public int getLongTermPartTimeAssessmentYear() {
		return longTermPartTimeAssessmentYear;
	}

	/**
	 * return Long Term Part Time Assessment Year
	 * @param longTermPartTimeAssessmentYear
	 */
	public void setLongTermPartTimeAssessmentYear(int longTermPartTimeAssessmentYear) {
		this.longTermPartTimeAssessmentYear = longTermPartTimeAssessmentYear;
	}

	/**
	 * get displayLongTermPartTimeAssessmentYearField
	 * @return displayLongTermPartTimeAssessmentYearField
	 */
	public boolean isDisplayLongTermPartTimeAssessmentYearField() {
		return displayLongTermPartTimeAssessmentYearField;
	}

	/**
	 * return displayLongTermPartTimeAssessmentYearField
	 * @param displayLongTermPartTimeAssessmentYearField
	 */
	public void setDisplayLongTermPartTimeAssessmentYearField(boolean displayLongTermPartTimeAssessmentYearField) {
		this.displayLongTermPartTimeAssessmentYearField = displayLongTermPartTimeAssessmentYearField;
	}
}