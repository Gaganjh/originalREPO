package com.manulife.pension.ps.web.tools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import com.manulife.pension.platform.web.taglib.util.LabelValueBean;

import com.manulife.pension.common.BaseSerializableCloneableObject;
import com.manulife.pension.propertyeditors.PropertyEditor;
import com.manulife.pension.ps.service.submission.dao.SubmissionConstants;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.census.CensusConstants;
import com.manulife.pension.ps.web.census.util.CensusLookups;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.tools.util.CensusDetailsHelper;
import com.manulife.pension.ps.web.util.CloneableAutoActionLabelForm;
import com.manulife.pension.service.employee.util.EmployeeData;
import com.manulife.pension.service.employee.util.EmployeeDataValidatorHelper;
import com.manulife.pension.service.employee.util.EmployeeValidationError;
import com.manulife.pension.service.employee.util.EmployeeValidationErrorCode;
import com.manulife.pension.service.employee.util.EmployeeValidationErrors;
import com.manulife.pension.service.employee.util.EmployeeData.Property;
import com.manulife.pension.service.employee.util.EmployeeValidationError.ErrorType;
import com.manulife.pension.service.employee.util.rules.EmployeeValidationConstants;
import com.manulife.pension.service.employee.valueobject.UserIdType;
import com.manulife.pension.util.StringUtility;

public class CensusErrorCorrectionForm extends CloneableAutoActionLabelForm {

	public interface SpecialPageType {
		
		public static final String similarSsn = "similarSsn"; // 13
		
		public static final String duplicateSubmittedSSN = "duplicateSubmittedSSN"; // 89
	
		public static final String multipleDuplicateEmployeeIdSortOptionNotEE // 211
			= "multipleDuplicateEmployeeIdSortOptionNotEE";
		
		public static final String duplicateEmployeeIdAccountHolderSortOptionNotEE  // 209
			= "duplicateEmployeeIdAccountHolderSortOptionNotEE";
		
		public static final String duplicateEmployeeIdNonAccountHolderSortOptionEE // 206
			= "duplicateEmployeeIdNonAccountHolderSortOptionEE";
		
		public static final String duplicateEmployeeIdNonAccountHolderSortOptionNotEE // 207
			= "duplicateEmployeeIdNonAccountHolderSortOptionNotEE";
		
		public static final String duplicateEmail = "duplicateEmailAddress";
		
		public static final String duplicateSubmittedEmail = "duplicateSubmittedEmailAddress";
	}
	
	public static final String TESTING = "Testing";
	
    private static final String MASKED_CURRENCY_STRING = "******";

    private static final String TRUNCATE_SUFFIX = "...";

    private static final String NAME_FIXED = "F";

    private static final long serialVersionUID = 1L;

    private static final boolean DISABLE_SSN = true;

    public static class DisplayProperty extends BaseSerializableCloneableObject {


        private static final long serialVersionUID = 1L;

        private boolean errorField;

        private boolean warningField;

        private String onlineValue;

        private boolean initialDisplay;

        private boolean disabled;

        private String toolTip;


        public DisplayProperty(String onlineValue, boolean initialDisplay) {
            this.onlineValue = onlineValue;
            this.initialDisplay = initialDisplay;
        }

        public boolean isWarningField() {
            return warningField && !errorField;
        }

        public void setWarningField(boolean warningField) {
            if (!this.warningField) {
                this.warningField = warningField;
            }
        }

        public boolean isErrorField() {
            return errorField;
        }

        public void setErrorField(boolean errorField) {
            if (!this.errorField) {
                this.errorField = errorField;
            }
        }

        public String getOnlineValue() {
            return onlineValue;
        }

        public boolean isInitialDisplay() {
            return initialDisplay;
        }

        public void setInitialDisplay(boolean initialDisplay) {
            this.initialDisplay = initialDisplay;
        }

        public boolean isDisabled() {
            return disabled;
        }

        public void setDisabled(boolean disabled) {
            this.disabled = disabled;
        }

        public String getToolTip() {
            return toolTip;
        }

        public void setToolTip(String toolTip) {
            this.toolTip = toolTip;
        }

        public void resetWarningField() {
            this.warningField = false;
        }

        public void resetErrorField() {
            this.errorField = false;
        }
    }


    /**
     * A map of values set in this form. Form values are always string based.
     */
    private Map<String, String> formValues = new HashMap<String, String>();

    public void setFormValues(Map<String, String> formValues) {
		this.formValues = formValues;
	}

	private Map<String, DisplayProperty> displayProperties = new HashMap<String, DisplayProperty>();

    private EmployeeValidationErrors errors = null;

    private Integer submissionId;

    private Integer contractId;

    private String employerDesignatedId;

    private Integer sourceRecordNo;

    private Integer sequenceNumber;

    private EmployeeData employeeData;

    private Integer numberOfErrorsInSubmission;

    private String submissionCaseTypeCode;

    private Map<String, List<LabelValueBean>> lookupData;
    private List<LabelValueBean> stateList = CensusLookups
			.getInstance().getLookup(CensusLookups.State);
    
    private List<LabelValueBean> countryList = CensusLookups
			.getInstance().getLookup(CensusLookups.Country);
    private List<LabelValueBean> prefixList = CensusLookups
			.getInstance().getLookup(CensusLookups.NamePrefix);
    private List<LabelValueBean> stateOfResList = CensusLookups
			.getInstance().getLookup(CensusLookups.StateOfResidence);
    private List<LabelValueBean> empStatusList = CensusLookups
			.getInstance().getLookup(CensusLookups.EmploymentStatus);
    private List<LabelValueBean> optOutInd = CensusLookups
			.getInstance().getOptOutInd();


    

	public List<LabelValueBean> getOptOutInd() {
		return optOutInd;
	}

	public void setOptOutInd(List<LabelValueBean> optOutInd) {
		this.optOutInd = optOutInd;
	}

	public List<LabelValueBean> getPrefixList() {
		return prefixList;
	}

	public void setPrefixList(List<LabelValueBean> prefixList) {
		this.prefixList = prefixList;
	}

	public List<LabelValueBean> getStateOfResList() {
		return stateOfResList;
	}

	public void setStateOfResList(List<LabelValueBean> stateOfResList) {
		this.stateOfResList = stateOfResList;
	}

	public List<LabelValueBean> getEmpStatusList() {
		return empStatusList;
	}

	public void setEmpStatusList(List<LabelValueBean> empStatusList) {
		this.empStatusList = empStatusList;
	}

	private String firstName;

    private String lastName;

    private String middleInitial;

    private boolean warningPage;

    private String participantSortOptionCode;

    private boolean similarSsnAccepted;

    private boolean newEmployee;

    private boolean maskSensitiveInformation;

    private String fullNameParsingErrorInd;

    private String specialPageType;
    
    private boolean ignoreEmployeeIdRules;

    public CensusErrorCorrectionForm() {
        // empty ctor
    }

    public List<LabelValueBean> getStateList() {
		return stateList;
	}

	public void setStateList(List<LabelValueBean> stateList) {
		this.stateList = stateList;
	}
	public List<LabelValueBean> getCountryList() {
		return countryList;
	}

	public void setCountryList(List<LabelValueBean> countryList) {
		this.countryList = countryList;
	}

    public void initializeFormValuesAndDisplayProperties(
            String participantSortOptionCode, EmployeeData submittedEmployee,
            EmployeeData employee, EmployeeValidationErrors errors,
            boolean maskSensitiveInformation) {

        warningPage = false;
        this.errors = errors;
        this.maskSensitiveInformation = maskSensitiveInformation;
        this.ignoreEmployeeIdRules = false;

        employeeData = (EmployeeData) submittedEmployee.clone();

        submissionCaseTypeCode = submittedEmployee.getSubmissionCaseTypeCode();
        sourceRecordNo = submittedEmployee.getSourceRecordNo();
        fullNameParsingErrorInd = submittedEmployee.getFullNameParsingErrorInd();

        this.participantSortOptionCode = participantSortOptionCode;
        if (employee == null) {
            newEmployee = true;
        }

        populateDisplayProperties(employee);
        populateFormValues(submittedEmployee);

        if (employee != null && !StringUtils.isEmpty(employee.getFirstName())) {
            firstName = StringUtils.trimToNull(employee.getFirstName());
        } else {
            firstName = StringUtils
                    .trimToNull(submittedEmployee.getFirstName());
        }
        if (employee != null && !StringUtils.isEmpty(employee.getLastName())) {
            lastName = StringUtils.trimToNull(employee.getLastName());
        } else {
            lastName = StringUtils.trimToNull(submittedEmployee.getLastName());
        }
        if (employee != null && !StringUtils.isEmpty(employee.getMiddleInit())) {
            middleInitial = StringUtils.trimToNull(employee.getMiddleInit());
        } else {
            middleInitial = StringUtils.trimToNull(submittedEmployee
                    .getMiddleInit());
        }
    }


    /**
     * Returns an employee data that is populated with values from the form.
     *
     * @return
     */
    public EmployeeData getEmployeeData(UserProfile user) {
        boolean nameHasChanged = false;
        String value;
        for (Property property : EmployeeData.PROPERTY_EDITORS.keySet()) {
            if (property.getName().equals(Property.SOCIAL_SECURITY_NUMBER.getName())) {
                // unformat SSN
                value = getFormValue(property.getName()).replaceAll("-","");
            } else if (property.getName().contains("Date")) {
                value = getFormValue(property.getName());

                String[] splittedDates = value.split("/");
                if (splittedDates.length == 3) {
                    for (int i = 0; i < splittedDates.length; i++) {
                        if (i < 2) {
                            splittedDates[i] = StringUtils.leftPad(splittedDates[i], 2, "0");
                        }
                    }
                    String tempValue = splittedDates[0] + splittedDates[1] + splittedDates[2];

                    // if valid date pad day and month to be 2 characters long and remove slashes
                    if (EmployeeData.getConvertedDate(tempValue) != null) {
                        value = tempValue;
                    }
                }

                // else leave as is

            } else if (property.getName().equals(Property.ANNUAL_BASE_SALARY.getName()) ||
                       property.getName().equals(Property.PLAN_YTD_COMPENSATION.getName())) {
                // unformat amounts
                value = getFormValue(property.getName()).replaceAll(",","");
            } else if (property.getName().equals(Property.LAST_NAME.getName())) {
                // check if value has been changed to reset the fullNameParsingErrorInd
                value = getFormValue(property.getName());
                if (!value.equals(employeeData.getLastName())) {
                    nameHasChanged = true;
                }
            } else if (property.getName().equals(Property.FIRST_NAME.getName())) {
                // check if value has been changed to reset the fullNameParsingErrorInd
                value = getFormValue(property.getName());
                if (StringUtils.isNotBlank(value)) {
                    nameHasChanged = true;
                }
            } else if (property.getName().equals(Property.MIDDLE_INIT.getName())) {
                // check if value has been changed to reset the fullNameParsingErrorInd
                value = getFormValue(property.getName());
                if (StringUtils.isNotBlank(value)) {
                    nameHasChanged = true;
                }
            } else if (property.getName().equals(Property.EMPLOYEE_NUMBER.getName())) {
                // check if value should be padded
                value = getFormValue(property.getName());
                if (StringUtils.isNotBlank(value)) {
                	value = StringUtils.upperCase(StringUtils.leftPad(value, 9, '0'));
                }                
            } else {
                // leave as is
                value = getFormValue(property.getName());
            }
            employeeData.setProperty(property, StringUtils
                    .trimToNull(value));
        }

        employeeData.setContractId(contractId);
        employeeData.setSubmissionId(submissionId);
        employeeData.setSequenceNumber(sequenceNumber);
        employeeData.setEmployerDesignatedId(employerDesignatedId);
        employeeData.setSourceRecordNo(sourceRecordNo);
        employeeData.setSubmissionCaseTypeCode(submissionCaseTypeCode);
        // User profile ID is set in EmployeeDataDAO
        //employeeData.setUserProfileId(user.getPrincipal().getProfileId());
        employeeData
                .setUserTypeCode(user.isInternalUser() ? UserIdType.UP_INTERNAL
                        : UserIdType.UP_EXTERNAL);
        employeeData.setCreatedUserFirstName(user.getPrincipal().getFirstName());
        employeeData.setCreatedUserLastName(user.getPrincipal().getLastName());
        // if name parsing error was encountered and name has been changed by the user, reset the flag
        if (Constants.YES.equals(fullNameParsingErrorInd) && nameHasChanged) {
            employeeData.setFullNameParsingErrorInd(NAME_FIXED);
        }
        employeeData.setConfirmed(true);
        return employeeData;
    }
    
    /**
     * Retrieve the property value that is suitable for display. Use the
     * associated property editor to format the value.
     *
     * @param bean
     * @param property
     * @return
     */
    private String getPropertyValue(Object bean, Property property) {
        if (bean == null) {
            return "";
        }
        PropertyEditor editor = EmployeeData.PROPERTY_EDITORS.get(property);
        return editor.getPropertyValue(bean);
    }

    /**
     * Returns related properties for display. Refer to DFS CEC.45
     *
     * @param property
     * @return
     */
    private Property[] getRelatedProperties(Property property) {

        if (Property.EMPLOYMENT_STATUS.equals(property)
                || Property.EMPLOYMENT_STATUS_DATE.equals(property)) {
            return new Property[] { Property.EMPLOYMENT_STATUS,
                    Property.EMPLOYMENT_STATUS_DATE };
        } else if (Property.ELIGIBILITY_DATE.equals(property)
                || Property.ELIGIBILITY_INDICATOR.equals(property)) {
            return new Property[] { Property.ELIGIBILITY_DATE,
                    Property.ELIGIBILITY_INDICATOR };
        } else if (Property.PLAN_YTD_HOURS_WORKED.equals(property)
                || Property.PLAN_YTD_COMPENSATION.equals(property)
                || Property.PLAN_YTD_HOURS_WORKED_COMPENSATION_EFFECTIVE_DATE
                        .equals(property)) {
            return new Property[] { Property.PLAN_YTD_HOURS_WORKED,
                    Property.PLAN_YTD_COMPENSATION,
                    Property.PLAN_YTD_HOURS_WORKED_COMPENSATION_EFFECTIVE_DATE };
        } else if (Property.LAST_NAME.equals(property)) {
            if (errors.contains(Property.LAST_NAME, EmployeeValidationErrorCode.FullNameNotParsed, ErrorType.error)) {
                return new Property[] { Property.FIRST_NAME,
                        Property.LAST_NAME,
                        Property.MIDDLE_INIT };
            }
        }

        /*
         * Address properties.
         */
        for (Property addressProperty : EmployeeData.ADDRESS_PROPERTIES) {
            if (addressProperty.equals(property)) {
                return EmployeeData.ADDRESS_PROPERTIES;
            }
        }

        return new Property[] {};
    }

    /**
     * Using the given list of errors, mark fields as either not shown on the
     * screen (if displayProperties.get(property) is null) or read only, if
     * displayProperties(property).getReadOnly == true. Display properties for
     * related fields are shown as well.
     *
     * @param errors
     */
    private void populateDisplayProperties(EmployeeData onlineEmployee) {

        displayProperties.clear();

        /*
         * Set display properties for the SSN field.
         */
        if (similarSsnAccepted) {
            Property[] similarSsnRelatedProperties = new Property[] {
                    Property.SOCIAL_SECURITY_NUMBER, Property.FIRST_NAME,
                    Property.LAST_NAME };

            for (Property similarSsnRelatedProperty : similarSsnRelatedProperties) {
                /*
                 * If similar SSN is accepted, we remove the error
                 */
                errors.removeError(similarSsnRelatedProperty,
                        EmployeeValidationErrorCode.SimilarSSN);
            }
        }

        /*
         * Find out all the errors and make sure they are displayed on the page.
         */
        for (Property property : errors.getErrorFields()) {

            DisplayProperty displayProperty = displayProperties.get(property
                    .getName());
            if (displayProperty == null) {
                displayProperty = new DisplayProperty(getPropertyValue(
                        onlineEmployee, property), true);
                displayProperties.put(property.getName(), displayProperty);
            }

            for (EmployeeValidationError error : errors.getErrors(property)) {
                /*
                 * Checks whether there is any error/warning associated with the
                 * field.
                 */
                displayProperty.setErrorField(ErrorType.error.equals(error
                        .getType()));
                displayProperty.setWarningField(ErrorType.warning.equals(error
                        .getType()));
            }

            /*
             * Obtain all the related fields. These fields are predefined in the
             * spec.
             */
            Property[] relatedProperties = getRelatedProperties(property);

            for (Property relatedProperty : relatedProperties) {
                /*
                 * If the field is not initialized for display, we do it here.
                 * Notice that we don't need to set whether it's an error field
                 * or not because if it's an error field, it will be found in
                 * the outer loop.
                 */
                if (!displayProperties.containsKey(relatedProperty.getName())) {
                    displayProperty = new DisplayProperty(getPropertyValue(
                            onlineEmployee, relatedProperty), true);
                    displayProperties.put(relatedProperty.getName(),
                            displayProperty);
                }
            }
        }

        /*
         * Second part of the method is to populate the rest of the fields.
         * These fields are not setup for initial display.
         */
        for (Property property : EmployeeData.PROPERTY_EDITORS.keySet()) {
            if (!displayProperties.containsKey(property.getName())) {
                DisplayProperty displayProperty = new DisplayProperty(
                        getPropertyValue(onlineEmployee, property), false);
                displayProperties.put(property.getName(), displayProperty);
            }
        }

        disableSpecialDisplayProperties();
    }

    /**
     * Populate the form values from the submitted employee object.
     *
     * @param errors
     */
    private void populateFormValues(EmployeeData submittedEmployee) {

        formValues.clear();

        /*
         * Find out all the errors and make sure they are displayed on the page.
         */
        for (Property property : EmployeeData.PROPERTY_EDITORS.keySet()) {
            if (EmployeeDataValidatorHelper.isPropertySubmitted(
                    submittedEmployee, property)) {
                setFormValue(property.getName(), submittedEmployee
                        .getProperty(property));
            }
        }
    }

    public Map<String, DisplayProperty> getDisplayProperties() {
        return displayProperties;
    }

    public Map<String, String> getFormValues() {
        Map<String, String> result = new HashMap<String, String>();
        for (String key : formValues.keySet()) {
            result.put(key, getFormValue(key));
        }
        return result;
    }
    
	public String getHireDate() {
		return getFormValue("hireDate");
	}

	public void setHireDate(String hireDate) {

		formValues.put("hireDate", hireDate);
	}
	public String getBirthDate() {
		return getFormValue("birthDate");
	}

	public void setBirthDate(String birthDate) {

		formValues.put("birthDate", birthDate);
	}
	public String getEmploymentStatusDate() {
		return getFormValue("employmentStatusDate");
	}

	public void setEmploymentStatusDate(String employmentStatusDate) {

		formValues.put("employmentStatusDate", employmentStatusDate);
	}
	public String getEligibilityDate() {
		return getFormValue("eligibilityDate");
	}

	public void setEligibilityDate(String eligibilityDate) {

		formValues.put("eligibilityDate", eligibilityDate);
	}
	public String getBeforeTaxDeferralPercentage() {
		return getFormValue("beforeTaxDeferralPercentage");
	}

	public void setBeforeTaxDeferralPercentage(String beforeTaxDeferralPercentage) {

		formValues.put("beforeTaxDeferralPercentage", beforeTaxDeferralPercentage);
	}
	
	public String getEligibilityIndicator() {
		return getFormValue("eligibilityIndicator");
	}

	public void setEligibilityIndicator(String eligibilityIndicator) {

		formValues.put("eligibilityIndicator", eligibilityIndicator);
	}
	public String getDesignatedRothDeferralPct() {
		return getFormValue("designatedRothDeferralPct");
	}

	public void setDesignatedRothDeferralPct(String designatedRothDeferralPct) {

		formValues.put("designatedRothDeferralPct", designatedRothDeferralPct);
	}
	public String getPlanYTDHoursWorkedEffectiveDate() {
		return getFormValue("planYTDHoursWorkedEffectiveDate");
	}

	public void setPlanYTDHoursWorked(String planYTDHoursWorked) {

		formValues.put("planYTDHoursWorked", planYTDHoursWorked);
	}
	
	public String getDesignatedRothDeferralAmt() {
		return getFormValue("designatedRothDeferralAmt");
	}

	public void setDesignatedRothDeferralAmt(String designatedRothDeferralAmt) {

		formValues.put("designatedRothDeferralAmt", designatedRothDeferralAmt);
	}
	
	public String getBeforeTaxFlatDollarDeferral() {
		return getFormValue("beforeTaxFlatDollarDeferral");
	}

	public void setBeforeTaxFlatDollarDeferral(String beforeTaxFlatDollarDeferral) {

		formValues.put("beforeTaxFlatDollarDeferral", beforeTaxFlatDollarDeferral);
	}
	
	public String getPlanYTDHoursWorked() {
		return getFormValue("planYTDHoursWorked");
	}
	public void setPlanYTDHoursWorkedEffectiveDate(String planYTDHoursWorkedEffectiveDate) {

		formValues.put("planYTDHoursWorkedEffectiveDate", planYTDHoursWorkedEffectiveDate);
	}
	public String getPlanYTDCompensation() {
		return getFormValue("planYTDCompensation");
	}

	public void setPlanYTDCompensation(String planYTDCompensation) {

		formValues.put("planYTDCompensation", planYTDCompensation);
	}
	public String getAnnualBaseSalary() {
		return getFormValue("annualBaseSalary");
	}

	public void setAnnualBaseSalary(String annualBaseSalary) {

		formValues.put("annualBaseSalary", annualBaseSalary);
	}
	
	public String getFirstName () {
		return getFormValue("firstName");
	}

	public void setFirstName (String firstName ) {

		formValues.put("firstName ", firstName );
	}
	public String getLastName() {
		return getFormValue("lastName");
	}

	public void setLastName(String lastName) {

		formValues.put("lastName", lastName);
	}
	public String getMiddleInit() {
		return getFormValue("middleInit");
	}

	public void setMiddleInit(String middleInit) {

		formValues.put("middleInit", middleInit);
	}
	public String getAddressLine1() {
		return getFormValue("addressLine1");
	}

	public void setAddressLine1(String addressLine1) {

		formValues.put("addressLine1", addressLine1);
	}
	public String getAddressLine2() {
		return getFormValue("addressLine2");
	}

	public void setAddressLine2(String addressLine2) {

		formValues.put("addressLine2", addressLine2);
	}
	public String getCityName() {
		return getFormValue("cityName");
	}

	public void setCityName(String cityName) {

		formValues.put("cityName", cityName);
	}
	public String getEmployerProvidedEmail() {
		return getFormValue("employerProvidedEmail");
	}

	public void setEmployerProvidedEmail(String employerProvidedEmail) {

		formValues.put("employerProvidedEmail", employerProvidedEmail);
	}
	public String getDivision() {
		return getFormValue("division");
	}

	public void setDivision(String division) {

		formValues.put("division", division);
	}
	public String getEmployeeNumber() {
		return getFormValue("employeeNumber");
	}

	public void setEmployeeNumber(String employeeNumber) {

		formValues.put("employeeNumber", employeeNumber);
	}
	public String getNamePrefix() {
		return getFormValue("namePrefix");
	}

	public void setNamePrefix(String namePrefix) {

		formValues.put("namePrefix", namePrefix);
	}
	public String getStateOfResidence() {
		return getFormValue("stateOfResidence");
	}

	public void setStateOfResidence(String stateOfResidence) {

		formValues.put("stateOfResidence", stateOfResidence);
	}
	public String getemploymentStatus() {
		return getFormValue("employmentStatus");
	}

	public void setemploymentStatus(String employmentStatus) {

		formValues.put("employmentStatus", employmentStatus);
	}
	public String getOptOutIndicator() {
		return getFormValue("optOutIndicator");
	}

	public void setOptOutIndicator(String optOutIndicator) {

		formValues.put("optOutIndicator", optOutIndicator);
	}
	
	
	public String getStateCode() {
		return getFormValue("stateCode");
	}

	public void setStateCode(String stateCode) {

		formValues.put("stateCode", stateCode);
	}
	public String getCountryName() {
		return getFormValue("countryName");
	}

	public void setCountryName(String countryName) {

		formValues.put("countryName", countryName);
	}
	
	
	

    public void setDisplayProperties(
            Map<String, DisplayProperty> displayProperties) {
        this.displayProperties = displayProperties;
    }

    public EmployeeValidationErrors getErrors() {
        return errors;
    }

    public void setErrors(EmployeeValidationErrors errors) {
        this.errors = errors;
    }

    /**
     * Use this method to retrieve data in Struts.
     *
     * @param key
     * @return
     */
    public String getFormValue(String key) {
        if (key.startsWith(Property.SOCIAL_SECURITY_NUMBER.getName())) {
            /*
             * Special treatment for SSN (this is just a placeholder,
             * for now there is no special treatment)
             */
            String ssn = formValues.get(Property.SOCIAL_SECURITY_NUMBER
                    .getName());
            if (ssn == null) {
                return "";
            } else {
                return ssn;
            }

        } else if (key.startsWith(Property.ZIP_CODE.getName())
                && key.length() > Property.ZIP_CODE.getName().length()) {

            String zip = formValues.get(Property.ZIP_CODE.getName());
            if (zip == null) {
                return "";
            }
            zip = StringUtils.rightPad(zip, 9, " ");
            if (key.endsWith("1")) {
                return zip.substring(0, 5);
            } else if (key.endsWith("2")) {
                return zip.substring(5);
            }
        }

        String value = formValues.get(key);
        if (value == null) {
            return "";
        }

        return value;
    }
    public String getZipCode1() {


            String zip = formValues.get(Property.ZIP_CODE.getName());
            if (zip == null) {
                return "";
            }
            zip = StringUtils.rightPad(zip, 9, " ");
                return zip.substring(0, 5);
            
    }
    public String getZipCode2() {


        String zip = formValues.get(Property.ZIP_CODE.getName());
        if (zip == null) {
            return "";
        }
        zip = StringUtils.rightPad(zip, 9, " ");
        
            return zip.substring(5);
        

}

    /**
     * Use this method to set data in Struts.
     *
     * @param key
     * @param value
     */
    public void setFormValue(String key, String strValue) {

        String value = StringUtility.replaceNonIso8859_1Character(strValue,
                StringUtility.DEFAULT_NON_ISO_8859_1_SUB);

        if (key.equals(Property.SOCIAL_SECURITY_NUMBER.getName())) {

            /*
             * Special treatment for SSN.
             */
            String ssn = CensusDetailsHelper.getFormattedSsn(
                    value, false, displayProperties.get(Property.SOCIAL_SECURITY_NUMBER.getName()).isDisabled());
            formValues.put(Property.SOCIAL_SECURITY_NUMBER.getName(), ssn);

            // set the tool tip for SSN, only if truncated
            if (ssn.endsWith(TRUNCATE_SUFFIX)) {
                displayProperties.get(Property.SOCIAL_SECURITY_NUMBER.getName()).setToolTip(value);
            }


        } else if (key.equals(Property.EMPLOYEE_NUMBER.getName())) {

            /*
             * Special treatment for Employee Number.
             */
            String empNumber = CensusDetailsHelper.getFormattedEmpNumber(
                    value, displayProperties.get(Property.EMPLOYEE_NUMBER.getName()).isDisabled());
            formValues.put(Property.EMPLOYEE_NUMBER.getName(), empNumber);

            // set the tool tip for Employee Number, only if truncated
            if (empNumber.endsWith(TRUNCATE_SUFFIX)) {
                displayProperties.get(Property.EMPLOYEE_NUMBER.getName()).setToolTip(value);
            }



        } else if (key.startsWith(Property.ZIP_CODE.getName())
                && key.length() > Property.ZIP_CODE.getName().length()) {

            String formZipValue = (String) ObjectUtils.defaultIfNull(formValues
                    .get(Property.ZIP_CODE.getName()), "");

            /*
             * Special treatment for USA Zip.
             * The key is actually "zipCode1", "zipCode2"
             */
            StringBuffer zipString = new StringBuffer(StringUtils.rightPad(
                    formZipValue, 9, " "));
            if (key.endsWith("1")) {
                zipString.replace(0, 5, StringUtils.rightPad(value, 5, " "));
            } else if (key.endsWith("2")) {
                zipString.replace(5, zipString.length(), StringUtils.rightPad(value, 4, " "));
            }
            formValues.put(Property.ZIP_CODE.getName(), zipString.toString());

        } else if (key.equals(Property.ANNUAL_BASE_SALARY.getName())
                || key.equals(Property.PLAN_YTD_COMPENSATION.getName())) {
            /*
             * For annual base salary and plan ytd compenstaion, don't overwrite
             * the original content with the masked information.
             */
            if (!MASKED_CURRENCY_STRING.equals(StringUtils.trimToEmpty(value))) {
                formValues.put(key, value);
            }
        } else if (key.contains("Date")){
            /*
             * Format date from database as "MM/dd/yyyy"
             */
            String formattedDate = CensusDetailsHelper.formatDateForWeb(value);
            formValues.put(key, formattedDate);

        } else {
            formValues.put(key, value);
        }
    }

    public Integer getContractId() {
        return contractId;
    }

    public String getEmployerDesignatedId() {
        return employerDesignatedId;
    }

    public Integer getSubmissionId() {
        return submissionId;
    }

    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }

    public void setEmployerDesignatedId(String employerDesignatedId) {
        this.employerDesignatedId = employerDesignatedId;
    }

    public void setSubmissionId(Integer submissionId) {
        this.submissionId = submissionId;
    }

    public Integer getSourceRecordNo() {
        return sourceRecordNo;
    }

    public void setSourceRecordNo(Integer sourceRecordNo) {
        this.sourceRecordNo = sourceRecordNo;
    }

    public Integer getNumberOfErrorsInSubmission() {
        return numberOfErrorsInSubmission;
    }

    public void setNumberOfErrorsInSubmission(Integer numberOfErrorsInSubmission) {
        this.numberOfErrorsInSubmission = numberOfErrorsInSubmission;
    }

    public Map<String, List<LabelValueBean>> getLookupData() {
        return lookupData;
    }

    public void setLookupData(Map<String, List<LabelValueBean>> lookupData) {
        this.lookupData = lookupData;
    }

    public String getSubmissionCaseTypeCode() {
        return submissionCaseTypeCode;
    }

    public void setSubmissionCaseTypeCode(String submissionCaseTypeCode) {
        this.submissionCaseTypeCode = submissionCaseTypeCode;
    }

    public String getDisplayName() {
        StringBuffer displayName = new StringBuffer();

        if (lastName != null) {
            displayName.append(lastName);
        }
        if (firstName != null) {
            if (lastName != null) {
                displayName.append(", ");
            }
            displayName.append(firstName);
        }
        if (middleInitial != null) {
            if (lastName != null || firstName != null) {
                displayName.append(", ");
            }
            displayName.append(middleInitial);
        }
        return displayName.toString();
    }

    public boolean isWarningPage() {
        return warningPage;
    }

    public void setWarningPage(boolean disabled) {
        warningPage = disabled;
        setDisabled(disabled);
    }

    public void setDisabled(boolean disabled) {
        for (DisplayProperty displayProperty : displayProperties.values()) {
            displayProperty.setDisabled(disabled);
        }
        if (!disabled) {
            disableSpecialDisplayProperties();
        } 
    }
    
    public void setDisabled(boolean disabled, EmployeeValidationErrorCode errorCode) {
    	setDisabled(disabled);
    	enableSpecialDisplayProperty(errorCode);
    }

    private void disableSpecialDisplayProperties() {

        if (EmployeeValidationConstants.PRTSRTOD_EE
                .equals(participantSortOptionCode)) {
            displayProperties.get(Property.EMPLOYEE_NUMBER.getName())
                    .setDisabled(true);
        }
        if (DISABLE_SSN
                || EmployeeValidationConstants.PRTSRTOD_SS
                        .equals(participantSortOptionCode)) {
            displayProperties.get(Property.SOCIAL_SECURITY_NUMBER.getName())
                    .setDisabled(true);
        }

    }
    
    private void enableSpecialDisplayProperty(EmployeeValidationErrorCode errorCode) {
    	
    	if (EmployeeValidationErrorCode.MultipleDuplicateEmployeeIdSortOptionNotEE
    			.equals(errorCode) ||
    		EmployeeValidationErrorCode.DuplicateEmployeeIdAccountHolderSortOptionNotEE
    			.equals(errorCode) ||
    		EmployeeValidationErrorCode.DuplicateEmployeeIdNonAccountHolderSortOptionNotEE
    			.equals(errorCode)) {
    		
    		displayProperties.get(Property.EMPLOYEE_NUMBER.getName())
            	.setDisabled(false);
    	}
    	if(EmployeeValidationErrorCode.DuplicateEmailAddress.equals(errorCode) ||
    			EmployeeValidationErrorCode.DuplicateSubmittedEmailAddress.equals(errorCode) ) {
    		displayProperties.get(Property.EMPLOYER_PROVIDED_EMAIL.getName()).setDisabled(false);
    	}
    }
    
    public void resetEmployeeNumber() {
    	setFormValue(Property.EMPLOYEE_NUMBER.getName(), employeeData.getEmployeeNumber());
    }

    public boolean isSimilarSsnAccepted() {
        return similarSsnAccepted;
    }

    public void setSimilarSsnAccepted(boolean similarSsnAccepted) {
        this.similarSsnAccepted = similarSsnAccepted;
    }

    public String getSpecialPageType() {
        return specialPageType;
    }

    public void setSpecialPageType(String specialPageType) {
        this.specialPageType = specialPageType;
    }


    public String getAction() {
        if (StringUtils.isNotBlank(actionLabel)) {
            if (actionLabel.indexOf("Next record") != -1) {
                return "NextRecord";
            }
        }
        return super.getAction();
    }

    /**
     * Check if current country selection is USA
     *
     * @return
     */
    public boolean isCountryUSA() {
        String value = getFormValue(Property.COUNTRY_NAME.getName());
        // USA will be shown if country is empty
        if (StringUtils.isEmpty(value)) {
            return true;
        }

        if (value.equals(CensusConstants.USA)) {
            return true;
        }
        // if it is an invalid country, it becomes USA in UI
        if (!CensusLookups.getInstance().getCountrySet().contains(value)) {
            return true;
        }
        return false;
    }

    /**
     * Returns whether the submission is already processed. This can happen if
     * someone press the Back button and go back to a submitted record or if
     * someone manually change the URL to refer to this particular record.
     *
     * @return
     */
    public boolean isSubmissionProcessed() {
        return SubmissionConstants.Census.PROCESS_STATUS_CODE_COMPLETE
                .equals(employeeData.getProcessStatusCode())
                || SubmissionConstants.Census.PROCESS_STATUS_CODE_CANCELLED
                        .equals(employeeData.getProcessStatusCode());
    }

    public boolean isMaskSensitiveInformation() {
        return maskSensitiveInformation;
    }

    public void setMaskSensitiveInformation(boolean maskSensitiveInformation) {
        this.maskSensitiveInformation = maskSensitiveInformation;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

	public boolean isIgnoreEmployeeIdRules() {
		return ignoreEmployeeIdRules;
	}

	public void setIgnoreEmployeeIdRules(boolean ignoreEmployeeIdRules) {
		this.ignoreEmployeeIdRules = ignoreEmployeeIdRules;
	}
}
