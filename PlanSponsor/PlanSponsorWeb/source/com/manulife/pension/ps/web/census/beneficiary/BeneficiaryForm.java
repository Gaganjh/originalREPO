package com.manulife.pension.ps.web.census.beneficiary;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.ps.web.util.CloneableAutoForm;
import com.manulife.pension.ps.web.util.CloneableForm;
import com.manulife.pension.service.beneficiary.valueobject.BeneficiaryDesignation;
import com.manulife.pension.service.beneficiary.valueobject.BeneficiaryDesignationData;


/**
 * Form Bean for online Beneficiary designation
 * @author Tamilarasu Krishnamoorthy
 * 
 */
public class BeneficiaryForm extends CloneableAutoForm {

	private static final long serialVersionUID = 1L;

	private boolean allowedToEdit;
	private boolean primaryBeneficiaryExists;
	private boolean contingentBeneficiaryExists;
	private String employeeFirstName;
	private String employeeLastName;
	private String employeeSSN;
	private String profileId;
	private String beneficiaryType = null;
	private String employeelastUpdatedBy;
	private Date employeelastUpdatedDate;
	private boolean beneficiaryRecordExisting;
	private CloneableForm clonedForm;
	private List<BeneficiaryDesignation> primaryBeneficiaries = null;
	private List<BeneficiaryDesignation> contingentBeneficiaries = null;
	private static TreeMap<String,String> relationship=new TreeMap<String,String>();
	private static Map<String,String> beneficiaryTypeMap=new HashMap<String,String>();

	

	
	/**
	 * constructor	
	 */
	public BeneficiaryForm() {
		primaryBeneficiaries = new ArrayList<BeneficiaryDesignation>();
		contingentBeneficiaries = new ArrayList<BeneficiaryDesignation>();
		primaryBeneficiaries.add(new BeneficiaryDesignation());
	}

	/**
	 * Get the last updated information
	 * @return String employeelastUpdatedBy
	 */
	public String getEmployeelastUpdatedBy() {
		return employeelastUpdatedBy;
	}

	/**
	 * Set the last updated information
	 * @param employeelastUpdatedBy
	 */
	public void setEmployeelastUpdatedBy(String employeelastUpdatedBy) {
		this.employeelastUpdatedBy = employeelastUpdatedBy;
	}
	
	/**
	 * Get the beneficiary type from Map
	 * @return Map<String, String>
	 */
	public Map<String, String> getBeneficiaryTypeMap() {
		return beneficiaryTypeMap;
	}
	
	/**
	 * Set the BeneficiaryType in Map
	 * 
	 * @param beneficiaryTypeMap
	 */
	@SuppressWarnings("static-access")
	public void setBeneficiaryTypeMap(Map<String, String> beneficiaryTypeMap) {
		this.beneficiaryTypeMap = beneficiaryTypeMap;
	}

	/**
	 * Get the last updated date
	 * @return Date employeelastUpdatedDate
	 */
	public Date getEmployeelastUpdatedDate() {
		return employeelastUpdatedDate;
	}

	/**
	 * Set the last updated date
	 * @param employeelastUpdatedDate
	 */
	public void setEmployeelastUpdatedDate(Date employeelastUpdatedDate) {
		this.employeelastUpdatedDate = employeelastUpdatedDate;
	}

	/**
	 * Get the relationship map
	 * @return Map<String, String>
	 */
	public Map<String, String> getRelationship() {
		return relationship;
	}
	
	/**
	 * Set the relationship map
	 * @param relationship
	 */
	@SuppressWarnings("static-access")
	public void setRelationship(TreeMap<String, String> relationship) {
		this.relationship = relationship;
	}
	/**
	 * Get the profileId
	 * @return profileId in string
	 */
	public String getProfileId() {
		return profileId;
	}

	/**
	 * Set the profileId
	 * @param profileId
	 */
	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

	/**
	 * Return true if primary Beneficiary contain  in list of beneficiary records
	 * @return Boolean  
	 */
	public boolean isPrimaryBeneficiaryExists() {
		return primaryBeneficiaryExists;
	}

	/**
	 * Set the primary Beneficiary to true if primaryBeneficiary record in the beneficiaryList
	 * @param primaryBeneficiaryFilledOrEmpty
	 */
	public void setPrimaryBeneficiaryExists(
			boolean primaryBeneficiaryExists) {
		this.primaryBeneficiaryExists = primaryBeneficiaryExists;
	}

	/**
	 *Return true if Contingent Beneficiary contain  in list of beneficiary records
	 * @return Boolean  
	 */
	public boolean isContingentBeneficiaryExists() {
		return contingentBeneficiaryExists;
	}

	/**
	 * Set the Contingent Beneficiary to true if primaryBeneficiary record in the beneficiaryList
	 * @param ContingentBeneficiaryFilledOrEmpty
	 */
	public void setContingentBeneficiaryExists(
			boolean contingentBeneficiaryExists) {
		this.contingentBeneficiaryExists = contingentBeneficiaryExists;
	}

	/**
	 * Get the Employee FirstName
	 * @return String
	 */
	public String getEmployeeFirstName() {
		return employeeFirstName;
	}

	/**
	 * Set the Employee First Name
	 * @param employeeFirstName
	 */
	public void setEmployeeFirstName(String employeeFirstName) {
		this.employeeFirstName = employeeFirstName;
	}

	/**
	 * Get the Employee LastName
	 * @return String
	 */
	public String getEmployeeLastName() {
		return employeeLastName;
	}

	/**
	 * Set the Employee Last Name
	 * @param employeeFirstName
	 */
	public void setEmployeeLastName(String employeeLastName) {
		this.employeeLastName = employeeLastName;
	}

	/**
	 * Get the Employee SSN
	 * @return String
	 */
	public String getEmployeeSSN() {
		return employeeSSN;
	}

	/**
	 * Set the Employee SSN
	 * @param employeeSSN
	 */
	public void setEmployeeSSN(String employeeSSN) {
		this.employeeSSN = employeeSSN;
	}

	/**
	 * Get the Beneficiary Type
	 * @return String As "1"  for primary and "2" for Contingent
	 */
	public String getBeneficiaryType() {
		return beneficiaryType;
	}

	/**
	 * Set the beneficiary Type 
	 * @param beneficiaryType
	 */
	public void setBeneficiaryType(String beneficiaryType) {
		this.beneficiaryType = beneficiaryType;
	}

	/**
	 * Get each primary beneficiary with the beneficiary Number 
	 * @param beneficiaryNumber
	 * @return BeneficiaryDesignation
	 * @throws ParseException 
	 */
	public BeneficiaryDesignation getPrimaryBenefeciary(int beneficiaryNumber)  {
		BeneficiaryDesignation beneficiaryDesignation = primaryBeneficiaries
		.get(beneficiaryNumber);
		return beneficiaryDesignation;
	}

	/**
	 * Set the primary Beneficiary information
	 * @param beneficiaryNumber
	 * @param benefeciaries
	 */
	public void setPrimaryBenefeciary(int beneficiaryNumber,
			BeneficiaryDesignation benefeciaries) {
		primaryBeneficiaries.set(beneficiaryNumber, benefeciaries);
	}

	/**
	 * Get each primary beneficiary with the beneficiary Number 
	 * @param beneficiaryNumber
	 * @return BeneficiaryDesignation
	 */
	public BeneficiaryDesignation getContingentBenefeciary(int beneficiaryNumber) {
		BeneficiaryDesignation beneficiaryDesignation = contingentBeneficiaries
		.get(beneficiaryNumber);
		return beneficiaryDesignation;
	}

	/**
	 *  Set the Contingent Beneficiary information
	 * @param beneficiaryNumber
	 * @param benefeciaries
	 */
	public void setContingentBenefeciary(int beneficiaryNumber,
			BeneficiaryDesignation benefeciaries) {
		contingentBeneficiaries.set(beneficiaryNumber, benefeciaries);
	}

	/**
	 * Get the list of Primary Beneficiary records.
	 * Add a primary beneficiary to the list, if it list is empty.
	 * @return List of BeneficiaryDesignation
	 */
	public List<BeneficiaryDesignation> getPrimaryBeneficiaries() {
		if (primaryBeneficiaries .size()==0) {
			primaryBeneficiaries = new ArrayList<BeneficiaryDesignation>();
			BeneficiaryDesignation beneficiaryDesignation = new BeneficiaryDesignation();
			primaryBeneficiaries.add(beneficiaryDesignation);
		}
		return primaryBeneficiaries;
	}

	/**
	 * Set the list of primary Beneficiary record
	 * @param primaryBeneficiary
	 */
	public void setPrimaryBeneficiaries(
			List<BeneficiaryDesignation> primaryBeneficiary) {
		this.primaryBeneficiaries = primaryBeneficiary;
	}

	/**
	 * Get the list of Contingent Beneficiary records
	 * Add a contingent beneficiary to the list, if it list is empty.
	 * @return List of BeneficiaryDesignation
	 */
	public List<BeneficiaryDesignation> getContingentBeneficiaries() {
		if (contingentBeneficiaries .size()==0) {
			contingentBeneficiaries = new ArrayList<BeneficiaryDesignation>();
			BeneficiaryDesignation beneficiaryDesignation = new BeneficiaryDesignation();
			contingentBeneficiaries.add(beneficiaryDesignation);
		}
		return contingentBeneficiaries;
	}

	/**
	 * Set the list of Contingent Beneficiary record
	 * @param ContingentBeneficiary
	 */
	public void setContingentBeneficiaries(
			List<BeneficiaryDesignation> ContingentBeneficiaries) {
		this.contingentBeneficiaries = ContingentBeneficiaries;
	}



	/**
	 * Add an element to the primary beneficiary. This method will create a
	 * new BeneficiaryDesignation object, add it to the primary
	 * BeneficiaryDesignation list and will return it.
	 * 
	 * @return the newly added element.
	 */
	public BeneficiaryDesignation addPrimaryBeneficiary() {
		BeneficiaryDesignation BeneficiaryDesignation =
			new BeneficiaryDesignation();
		BeneficiaryDesignation.setBeneficiaryTypeCode(
				BeneficiaryDesignationData.PRIMARY_BENEFICIARY_TYPE_CODE);
		primaryBeneficiaries.add(BeneficiaryDesignation);
		return BeneficiaryDesignation;
	}

	/**
	 * Add an element to the Contingent beneficiary. This method will create a
	 * new BeneficiaryDesignation object, add it to the Contingent
	 * BeneficiaryDesignation list and will return it.
	 * 
	 * @return the newly added element.
	 */
	public BeneficiaryDesignation addContingentBeneficiary() {
		BeneficiaryDesignation BeneficiaryDesignation = 
			new BeneficiaryDesignation();
		BeneficiaryDesignation.setBeneficiaryTypeCode(
				BeneficiaryDesignationData.CONTINGENT_BENEFICIARY_TYPE_CODE);
		contingentBeneficiaries.add(BeneficiaryDesignation);

		return BeneficiaryDesignation;
	}

	/**
	 * This method will be called by Struts Controller to reset the form values
	 * 	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param request
	 *            HttpServletRequest
	 * 
	 */

	public void reset( HttpServletRequest request) {
		this.action = DEFAULT;
		this.beneficiaryType="";
		this.beneficiaryRecordExisting = false;
		for(BeneficiaryDesignation beneficiaryDesignation:primaryBeneficiaries){
			beneficiaryDesignation.setBeneficiaryDeleted(false);
		}
		for(BeneficiaryDesignation beneficiaryDesignation:contingentBeneficiaries){
			beneficiaryDesignation.setBeneficiaryDeleted(false);
		}
	}

	/**
	 * Method to test whether given beneficiary Is empty or Not
	 * @param beneficiaryDesignation
	 * @return
	 */
	public Boolean isBeneficiaryEmpty(BeneficiaryDesignation beneficiaryDesignation ){
		if(StringUtils.isEmpty(beneficiaryDesignation.getFirstName().trim()) && StringUtils.isEmpty(beneficiaryDesignation.getLastName().trim())
				&& StringUtils.isEmpty(beneficiaryDesignation.getStringBirthDate().trim())&& StringUtils.isEmpty(beneficiaryDesignation.getRelationshipCode().trim())
				&& StringUtils.isEmpty(beneficiaryDesignation.getStringSharePct().trim())){
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * @return the allowedToEdit
	 */
	public boolean isAllowedToEdit() {
		return allowedToEdit;
	}

	/**
	 * @param allowedToEdit
	 *            the allowedToEdit to set
	 */
	public void setAllowedToEdit(boolean allowedToEdit) {
		this.allowedToEdit = allowedToEdit;
	}
	
	/**
	 * Return boolean value true, if there existing beneficiaryRecord in the database
	 * @return boolean
	 */
	public boolean isBeneficiaryRecordExisting() {
		return beneficiaryRecordExisting;
	}
	
	/**
	 * Set the boolean value,if there existing beneficiaryRecord in the database
	 * @param beneficiaryRecordExisting
	 */
	public void setBeneficiaryRecordExisting(boolean beneficiaryRecordExisting) {
		this.beneficiaryRecordExisting = beneficiaryRecordExisting;
	}
	
	/**
	 * Get the clonedForm
	 * @return CloneableForm
	 */
	public CloneableForm getClonedForm() {
		return clonedForm;
	}
	
	/**
	 * Store the clone form to cloneForm
	 */
	public void storeClonedForm() {
		clonedForm = (CloneableForm) cloneForm();
	}
	
	/**
	 * only clone the updatable values There is some workaround. And Don't use
	 * this clonedform to reset the values, always use database's record to
	 * reset values.
	 *
	 * @return CloneableForm
	 */
	public CloneableForm  cloneForm() {
		List<BeneficiaryDesignation> clonedPrimaryBeneficiaries = new ArrayList<BeneficiaryDesignation>();
		List<BeneficiaryDesignation> clonedContingentBeneficiaries = new ArrayList<BeneficiaryDesignation>();
		BeneficiaryForm cloned = new BeneficiaryForm();
		for(BeneficiaryDesignation beneficiaryDesignation:getPrimaryBeneficiaries()){
			clonedPrimaryBeneficiaries.add(new BeneficiaryDesignation(beneficiaryDesignation.getBeneficiaryNo(),beneficiaryDesignation.getRelationshipCode(),
					beneficiaryDesignation.getOtherRelationshipDesc(),beneficiaryDesignation.getFirstName(),
					beneficiaryDesignation.getLastName(),beneficiaryDesignation.getBirthDate(),
					beneficiaryDesignation.getBeneficiaryTypeCode(),beneficiaryDesignation.getSharePct()));

		}
		cloned.setPrimaryBeneficiaries(clonedPrimaryBeneficiaries);
		for(BeneficiaryDesignation beneficiaryDesignation:getContingentBeneficiaries()){
			clonedContingentBeneficiaries.add(new BeneficiaryDesignation(beneficiaryDesignation.getBeneficiaryNo(),beneficiaryDesignation.getRelationshipCode(),
					beneficiaryDesignation.getOtherRelationshipDesc(),beneficiaryDesignation.getFirstName(),
					beneficiaryDesignation.getLastName(),beneficiaryDesignation.getBirthDate(),
					beneficiaryDesignation.getBeneficiaryTypeCode(),beneficiaryDesignation.getSharePct()));

		}
		cloned.setContingentBeneficiaries(clonedContingentBeneficiaries);
		cloned.getRelationship().putAll(getRelationship());
		return cloned;
	}




}
