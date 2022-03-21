package com.manulife.pension.ps.web.census;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.apache.log4j.Logger;
import org.owasp.encoder.Encode;

import com.manulife.pension.platform.web.taglib.util.LabelValueBean;

import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.Miscellaneous;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.EmployeeServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.propertyeditors.CurrencyPropertyEditor;
import com.manulife.pension.propertyeditors.DatePropertyEditor;
import com.manulife.pension.propertyeditors.IntegerPropertyEditor;
import com.manulife.pension.propertyeditors.PercentagePropertyEditor;
import com.manulife.pension.propertyeditors.PropertyEditor;
import com.manulife.pension.propertyeditors.StringPropertyEditor;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.census.util.CensusLookups;
import com.manulife.pension.ps.web.census.util.EmployeeSnapshotFormFieldValidationRules;
import com.manulife.pension.ps.web.census.util.FormFieldValidationRuleSupportForm;
import com.manulife.pension.ps.web.content.ContentConstants;
import com.manulife.pension.ps.web.contract.csf.EligibilityCalculationMoneyType;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.CloneableForm;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.employee.valueobject.AddressVO;
import com.manulife.pension.service.employee.valueobject.Employee;
import com.manulife.pension.service.employee.valueobject.EmployeeDetailVO;
import com.manulife.pension.service.employee.valueobject.EmployeeVestingVO;
import com.manulife.pension.service.vesting.EmployeeVestingInformation;
import com.manulife.pension.service.vesting.VestingConstants;
import com.manulife.pension.service.vesting.VestingInputDescription;
import com.manulife.pension.service.vesting.VestingRetrievalDetails;
import com.manulife.pension.service.vesting.util.PlanYear;
import com.manulife.pension.util.content.manager.ContentCacheManager;
import com.manulife.pension.validator.ValidationError;
import com.manulife.util.render.RenderConstants;

/**
 * The Form to hold EmployeeSnapshot information for editing.
 *
 * NOTE: DO Not user values map directly. Always user getValue method to get a
 * value of a field !!!
 *
 * @author guweigu
 *
 */

public class EditEmployeeSnapshotForm extends EmployeeSnapshotForm
		implements EmployeeSnapshotProperties,
		FormFieldValidationRuleSupportForm, CloneableForm {

	private static final Logger log = Logger.getLogger(EditEmployeeSnapshotForm.class);
	
	public interface PageErrorType {
		public static final String duplicateEmail = "duplicateEmailAddress";
	}
	/**
	 * The supported Date format for the employee snapshot form.
	 */
	private static final String DateFormat = RenderConstants.MEDIUM_MDY_SLASHED;

	private static final FastDateFormat dateFormatter = FastDateFormat
			.getInstance(DateFormat);

	private static final long serialVersionUID = -7538195449234033268L;

	/**
	 * Map between the field name and the property editor
	 */
	private static Map<String, PropertyEditor> propertyMap = new HashMap<String, PropertyEditor>();

	/**
	 * Set of field name for address
	 */
	private static Set<String> addressFieldSet = new HashSet<String>();

	/**
	 * Stores the field values. The key is the filed name The value is the input
	 * string value
	 */
	private Map<String, String> values = new HashMap<String, String>();

	private CloneableForm clonedForm;

	// indicate if the employee is participant
	private boolean participant = false;

	// indicate if the form is in the readOnly format
	// the page should disable all the input field if it is true
	private boolean readOnly = false;

	// indicate if the user choose to accept similar ssn
	private boolean acceptSimilarSsn = false;

	/**
	 * Store the previous values before the lastest save
	 */
	private Map<String, String> previousValue;

	// special handling for SSN
	private SSN ssn = new SSN();

	// special handling for ZipCode
	private ZipCode zipCode = new ZipCode();

	// special handling for State code
	private AddressStateCode state = new AddressStateCode();
	
	// special handling for Duplicate Email
	private String specialPageType;

	private List<LabelValueBean> eligibleOptions = CensusLookups.getInstance()
			.getLookup(CensusLookups.BooleanString);

	private List<LabelValueBean> employmentStatusOptions = CensusLookups
			.getInstance().getLookup(CensusLookups.EmploymentStatus);

	private List<LabelValueBean> fullyVestedOverrideOptions = CensusLookups
			.getInstance().getLookup(CensusLookups.BooleanString);
	
	private List<LabelValueBean> stateList = CensusLookups
			.getInstance().getLookup(CensusLookups.State);
	private List<LabelValueBean> stateOfResidenceList = CensusLookups
			.getInstance().getLookup(CensusLookups.StateOfResidence);
	
	private List<LabelValueBean> longTermPartTimeAssessmentYearOptions = CensusLookups.getInstance()
			.getLookup(CensusLookups.IntegerValues);

	private List<LabelValueBean> previousYearsOptions = new ArrayList<LabelValueBean>(
			3);

	private EmployeeVestingInformation vestingInformation;

	private boolean disablePrevioysYrsEffDate = true;

	private boolean disableFullyVestedEffDate = true;
	
	//	 indicate if the user choose to accept similar ssn
	private boolean optOutDisabled = false;
	private boolean ae90DaysOptOutDisabled = false;

	private boolean showLinkToParticipantAcct = false;
	private boolean ae90DaysOptOutIndDatabaseSet;
    private boolean isEligibilityDateValueChanged;
    
	public boolean isEligibilityDateValueChanged() {
		return isEligibilityDateValueChanged;
	}

	public void setEligibilityDateValueChanged(boolean isEligibilityDateValueChanged) {
		this.isEligibilityDateValueChanged = isEligibilityDateValueChanged;
	}

	public boolean getAe90DaysOptOutIndDatabaseSet() {
		return ae90DaysOptOutIndDatabaseSet;
	}

	public void setAe90DaysOptOutIndDatabaseSet(boolean ae90DaysOptOutIndDatabaseSet) {
		this.ae90DaysOptOutIndDatabaseSet = ae90DaysOptOutIndDatabaseSet;
	}

	/**
	 * Setup the form based on the employee value object and the user profile
	 *
	 * @param employee
	 *            The employee value object (should be treated for UI friendly
	 * @see EmployeeSnapshotAction.getEmployeeForUI)
	 * @param profile
	 *            The user profile
	 */
	public void setUp(final Employee employee, final UserProfile profile) throws SystemException {
		Long pId = employee.getEmployeeDetailVO().getProfileId();

		setProfileId(pId == null ? null : pId.toString());
		if(employee.getEmployeeDetailVO().getContractId() == null){
		   employee.getEmployeeDetailVO().setContractId(profile.getCurrentContract().getContractNumber());
		}
		populate(employee);
		// set up the eligible options
		// if the value is not blank, then the options do not include
		// blank anymore.
		String value = getValue(EligibilityInd);
		if (StringUtils.isEmpty(value)) {
			eligibleOptions = CensusLookups.getInstance().getLookup(
					CensusLookups.YesNo);
		} else {
			eligibleOptions = CensusLookups.getInstance().getLookup(
					CensusLookups.YesNoWithoutBlank);
		}
		
		longTermPartTimeAssessmentYearOptions = CensusLookups.getInstance().getLookup(
					CensusLookups.IntegerValues);
		
		// set up employment status options based on the user role
		if (profile.isInternalUser() && !employee.isParticipant()) {
			employmentStatusOptions = CensusLookups.getInstance().getLookup(
					CensusLookups.EmploymentStatus);
		} else {
			employmentStatusOptions = CensusLookups.getInstance().getLookup(
					CensusLookups.EmploymentStatusWithoutC);
		}
		String employmentStatus = getValue(EmploymentStatus);
		// add blank into the list if it is not new employee and
		// the current value in csdb is blank
		if (StringUtils.isEmpty(employmentStatus)
				&& !StringUtils.isEmpty(profileId)) {
			List<LabelValueBean> options = new ArrayList<LabelValueBean>();
			// add the blank;
			options.add(new LabelValueBean("", ""));
			options.addAll(employmentStatusOptions);
			employmentStatusOptions = options;
		}
		// check the
		if (isHosBasedVesting()) {
			PlanYear year = new PlanYear(getPlanYearEnd(), new Date());
			previousYearsOptions.clear();
			previousYearsOptions.add(new LabelValueBean("", ""));
			String dValue = dateFormatter.format(year.getLastPlanYearEndDate());
			previousYearsOptions.add(new LabelValueBean(dValue, dValue));
			year.previousYear();
			dValue = dateFormatter.format(year.getLastPlanYearEndDate());
			previousYearsOptions.add(new LabelValueBean(dValue, dValue));
		}
		if (isShowVesting()) {
			EmployeeVestingInformation evi = getVestingInformation();
			if (evi == null) {
				EmployeeVestingInformation eviFromVestingEngine = employee
						.getEmployeeVestingVO().getEmployeeVestingInformation();
				if (eviFromVestingEngine != null) {
					setVestingInformation(eviFromVestingEngine);
				}
			}
		}
	}

	/**
	 * Using an Employee object to populate the form.
	 *
	 * @param employee
	 *            The Employee object used to populate the form.
	 */
	protected void populate(final Employee employee) throws SystemException{
		setParticipant(employee.isParticipantAccountInd());

		ssn.setValue(employee.getEmployeeDetailVO().getSocialSecurityNumber());
		for (Iterator it = propertyMap.keySet().iterator(); it.hasNext();) {
			String name = (String) it.next();
			values.put(name, ((PropertyEditor) propertyMap.get(name))
					.getPropertyValue(employee));
		}
		zipCode.setValue(employee.getAddressVO().getZipCode());
		state.setValue(employee.getAddressVO().getStateCode());
		ae90DaysOptOutIndDatabaseSet = (employee.getEmployeeDetailVO().getAe90DaysOptOutInd()!= null
				&& !"".equals(employee.getEmployeeDetailVO().getAe90DaysOptOutInd().trim())
				&& !" ".equals(employee.getEmployeeDetailVO().getAe90DaysOptOutInd().trim())) ? true:false;
		ae90DaysOptOutDisplayed = displayAe90DaysOptOut(employee.getEmployeeDetailVO().getContractId());
		optOutDisabled = isOptOutDisabledForEmployee(employee) || readOnly;
		ae90DaysOptOutDisabled = isAe90DaysWithdrawalDisabledForEmployee(employee) || readOnly;
		
		if(isParticipant() || 
		  (employee.getParticipantContract() != null && "NT".equals(employee.getParticipantContract().getParticipantStatusCode())))
		{
			showLinkToParticipantAcct = true;
		}
	}

	/**
	 * Using the form's input value to update the Employee object.
	 *
	 * @param employee
	 *            The Employee object to be updated.
	 */
	public void updateEmployee(final Employee employee,
			boolean setNullForInvalid) throws SystemException{
		// need to get the nested object ready for receiving values
		if (employee.getEmployeeDetailVO() == null) {
			employee.setEmployeeDetailVO(new EmployeeDetailVO());
		}

		if (employee.getAddressVO() == null) {
			employee.setAddressVO(new AddressVO());
		}

		if (employee.getEmployeeVestingVO() == null) {
			employee.setEmployeeVestingVO(new EmployeeVestingVO());
		}

		// Special treatment for OptOutInd
		String optOut = getValue(OptOut);
		String optOutOnCSDB = employee.getEmployeeDetailVO()
				.getAutoEnrollOptOutInd();
		// optOut is not nullable, so has to assign blank
		optOutOnCSDB = (optOutOnCSDB == null ? "" : optOutOnCSDB);
		String ae90DaysOptOutOnCSDB = employee.getEmployeeDetailVO().getAe90DaysOptOutInd();

		// check if previous years of service is changed
		// if not changed, do not set it
		EmployeeVestingInformation evi = employee.getEmployeeVestingVO()
				.getEmployeeVestingInformation();
		List<VestingInputDescription> pyosList = null;
		boolean resetPyos = false;
		if (evi != null
				&& evi.getRetrievalDetails() != null
				&& StringUtils.equals(VestingConstants.CreditingMethod.HOURS_OF_SERVICE, 
				        evi.getRetrievalDetails().getCreditingMethod())) {
			VestingRetrievalDetails fact = evi.getRetrievalDetails();
			pyosList = (List<VestingInputDescription>) fact.getInputs().get(VestingRetrievalDetails.PARAMETER_RAW_VESTED_YEARS_OF_SERVICE);
			Date pyosEffectiveDate = null;
			try {
				pyosEffectiveDate = (Date) getProperty(PreviousYearsOfServiceEffectiveDate);
			} catch (ParseException e) {
				// ignore the input then.... the validation code will catch this
			}

			if (pyosList != null && !pyosList.isEmpty() && pyosEffectiveDate!= null) {
				VestingInputDescription pyos = null;
				for (VestingInputDescription input : pyosList) {
				    if (pyosEffectiveDate.equals(input.getEffectiveDate())) {
				        pyos = input;
				        break;
				    }
				}
				Integer originalPyos = (pyos == null ? null : pyos.getIntegerValue());
				String pyosString = getValue(PreviousYearsOfService);
				Integer newPyos = null;
				// it should be a valid integer string already
				if (!StringUtils.isEmpty(pyosString)) {
					newPyos = new Integer(pyosString);
				}
				if (ObjectUtils.equals(originalPyos, newPyos)) {
					resetPyos = true;
				}
			}
		}


		BigDecimal btdp = employee.getEmployeeDetailVO().getBeforeTaxDeferralPct();
		BigDecimal btpa = employee.getEmployeeDetailVO().getBeforeTaxFlatDollarDeferral();
		BigDecimal rdp = employee.getEmployeeDetailVO().getDesignatedRothDeferralPct();
		BigDecimal rda = employee.getEmployeeDetailVO().getDesignatedRothDeferralAmt();
		Date eligibilityDate=employee.getEmployeeVestingVO().getEligibilityDate();

		for (Iterator it = propertyMap.keySet().iterator(); it.hasNext();) {
			String name = (String) it.next();
			((PropertyEditor) propertyMap.get(name)).setProperty(employee,
					(String) getValue(name), setNullForInvalid);
		}

		// mark as updated if changed
		if (valueUpdated(btdp, employee.getEmployeeDetailVO().getBeforeTaxDeferralPct()) ||
		    valueUpdated(btpa, employee.getEmployeeDetailVO().getBeforeTaxFlatDollarDeferral()) ||
		    valueUpdated(rdp, employee.getEmployeeDetailVO().getDesignatedRothDeferralPct()) ||
		    valueUpdated(rda, employee.getEmployeeDetailVO().getDesignatedRothDeferralAmt())) {
			employee.getEmployeeDetailVO().setDeferValuesLastUpdatedTS(new Timestamp(System.currentTimeMillis()));
		}
       
		if(valueUpdated(eligibilityDate,employee.getEmployeeVestingVO().getEligibilityDate())){
	     this.setEligibilityDateValueChanged(true);
       }
		if (resetPyos) {
			EmployeeVestingVO evo = employee.getEmployeeVestingVO();
			evo.setPreviousYearsOfServiceEffectiveDate(evo
					.getLastUpdatedpreviousYearsOfServiceEffectiveDate());
			evo.setPreviousYrsOfService(evo
					.getLastUpdatedPreviousYrsOfService());
		}

		// if the OptOut ind is blank on CSDB and on the page it is
		// not checked, don't update the value in CSDB
		if (StringUtils.isEmpty(StringUtils.trim(optOutOnCSDB))
				&& !"Y".equals(optOut)) {
			employee.getEmployeeDetailVO().setAutoEnrollOptOutInd(optOutOnCSDB);
		}
		// if add and employee did not opt out
		if(StringUtils.isEmpty(getProfileId()) && !"Y".equals(optOut) && employee.getEmployeeDetailVO().getContractId() != null)
		{
			  try{
			        ContractServiceDelegate delegate = ContractServiceDelegate.getInstance();  
			        ContractServiceFeature csf = delegate.getContractServiceFeature(
			        		employee.getEmployeeDetailVO().getContractId(), ServiceFeatureConstants.AUTO_ENROLLMENT_FEATURE);
			        if (csf != null && ServiceFeatureConstants.YES.equals(csf.getValue())) {
			        	employee.getEmployeeDetailVO().setAutoEnrollOptOutInd("N");
			        } 
		        } catch (ApplicationException e) {
		            throw new SystemException(e,"Error thrown while calling ae csf for " + employee.getEmployeeDetailVO().getContractId());
		        }
		}
		
		// EPS 383
		if("Y".equals(optOutOnCSDB) && !"Y".equals(optOut))
		{
			employee.getEmployeeDetailVO().setAutoEnrollOptOutInd("N");
			if(employee.getEmployeeDetailVO().getAe90DaysOptOutInd() != null) {
				employee.getEmployeeDetailVO().setAe90DaysOptOutInd(" ");
			}
		}
		// if changing 90 day opt out from a value to blank, ensure it gets set to blank, not null
		if(StringUtils.isNotEmpty(ae90DaysOptOutOnCSDB) && employee.getEmployeeDetailVO().getAe90DaysOptOutInd() == null) {
			employee.getEmployeeDetailVO().setAe90DaysOptOutInd(" ");
		}
		if(StringUtils.isBlank(ae90DaysOptOutOnCSDB) && StringUtils.isNotBlank(employee.getEmployeeDetailVO().getAe90DaysOptOutInd()))
		{
			showConfirmationToDo = true;
			
		} 
		else {
			showConfirmationToDo = false;
		}
		
		if((StringUtils.isBlank(ae90DaysOptOutOnCSDB) && StringUtils.isNotBlank(employee.getEmployeeDetailVO().getAe90DaysOptOutInd())) ||
			"Y".equals(employee.getEmployeeDetailVO().getAutoEnrollOptOutInd()))
		{
			try{
	        	Miscellaneous declineReason = (Miscellaneous)ContentCacheManager.getInstance().getContentById(ContentConstants.PENDING_REQUESTS_DECLINE_REASON, ContentTypeManager.instance().MISCELLANEOUS);
	        	employee.setAe90DaysWithdrawalRemarks(declineReason.getText());
	       
			}catch(ContentException ex){
				employee.setAe90DaysWithdrawalRemarks("Opted out of participation");
	    	}
		}
		
		// need to check if the address fields are all blanks
		AddressVO address = employee.getAddressVO();
		if (StringUtils.isEmpty(address.getAddressLine1())
				&& StringUtils.isEmpty(address.getAddressLine2())
				&& StringUtils.isEmpty(address.getCity())
				&& StringUtils.isEmpty(address.getStateCode())
				&& StringUtils.isEmpty(address.getZipCode())
				&& StringUtils.isEmpty(address.getCountry())) {
			employee.setAddressVO(null);
		}
	}

	// notEqual
	private boolean valueUpdated(BigDecimal value1, BigDecimal value2) {
		if (value1 == null) {
			return (value2 !=null);
		} else {
			if (value2==null) return true;
			return value1.doubleValue() != value2.doubleValue(); // don't use .equals
		}
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
	 * Setter for supporting the generic property value(propertyName) in the
	 * page.
	 *
	 * @param name
	 *            The property name.
	 * @param value
	 *            The string value of the property.
	 */
	public void setValue(final String name, final String value) {
		if (value == null) {
			values.remove(name);
		} else {
			values.put(name, value.trim());
			if (name.equals(SSN)) {
				ssn.setValue(value);
			}
		}
	}

	/**
	 * Getter for supporting the generic property value(propertyName) in the
	 * page. Always use this method to get value of a field !!!
	 *
	 * @param name
	 *            The property name
	 * @return The string value of the property
	 */
	public String getValue(final String name) {
		// First do special treatment for certain fields
		if (name.equals(OptOut)) {
			String optOut = values.get(OptOut);
			return optOut == null ? "N" : optOut;
		}
		if (name.equals(SSN)) {
			return Encode.forHtmlContent(ssn.getValue());
		}
		if (name.equals(ZipCode)) {
			return zipCode.getValue();
		}
		if (name.equals(State)) {
			return state.getValue();
		}

		return (String) values.get(name);
	}

	public Object getProperty(final String name) throws ParseException {
		return ((PropertyEditor) propertyMap.get(name)).convert(getValue(name));
	}

	/**
	 * Check if the field has valid type data
	 *
	 * @param name
	 * @return
	 */
	public boolean isFieldValueValidType(final String name) {
		PropertyEditor editor = propertyMap.get(name);
		if (editor == null || !editor.isValidFormat(getValue(name))) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * @return the lookups
	 */
	public CensusLookups getLookups() {
		return CensusLookups.getInstance();
	}

	public CloneableForm getClonedForm() {
		return clonedForm;
	}

	public void storeClonedForm() {
		clonedForm = (CloneableForm) cloneForm();
	}

	/**
	 * only clone the updateable values There is some workaround. And Don't use
	 * this clonedform to reset the values, always use database's record to
	 * reset values.
	 *
	 * @return
	 */
	protected CloneableForm cloneForm() {
		EditEmployeeSnapshotForm cloned = new EditEmployeeSnapshotForm();
		cloned.values = new HashMap<String, String>(values);
		cloned.getSsn().setValue(getSsn().getValue());
		cloned.getZipCode().setValue(getZipCode().getValue());
		cloned.getState().setValue(getState().getValue());
		return cloned;
	}

	/**
	 * This is not a full clone
	 */
	public Object clone() {
		EditEmployeeSnapshotForm cloned = new EditEmployeeSnapshotForm();
		cloned.values = new HashMap<String, String>(values);
		cloned.getSsn().setValue(getSsn().getValue());
		cloned.getZipCode().setValue(getZipCode().getValue());
		cloned.getState().setValue(getState().getValue());
		return cloned;
	}

	public void reset( HttpServletRequest request) {
		super.reset( request);
		participant = false;
		profileId = null;
		String submitted = request.getParameter("submitted");

		if (!readOnly) {
			if (submitted == null) {
				values.clear();
				ssn.reset();
				zipCode.reset();
				state.reset();
			} else {
				// clear the OptOut since it is a checkbox
				values.remove(OptOut);
				values.remove(Ae90DaysOptOut);
			}
		}
		readOnly = false;
		acceptSimilarSsn = false;
		showLinkToParticipantAcct = false;
	}

	public boolean isParticipant() {
		return participant;
	}

	public void setParticipant(boolean participant) {
		this.participant = participant;
	}
	
	public boolean isShowLinkToParticipantAcct() {
		return showLinkToParticipantAcct;
	}

	public void setShowLinkToParticipantAcct(boolean showLinkToParticipantAcct) {
		this.showLinkToParticipantAcct = showLinkToParticipantAcct;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
		if (readOnly) {
			setDisableFullyVestedEffDate(readOnly);
			setDisablePrevioysYrsEffDate(readOnly);
		}
	}

	public void validate(List<ValidationError> errors) {
		EmployeeSnapshotFormFieldValidationRules.getInstance().validate(this,
				errors);
/*		
        if (StringUtils.trimToNull(getValue(PreviousYearsOfService)) == null
				&& vestingInformation != null
				&& VestingConstants.CreditingMethod.HOURS_OF_SERVICE
						.equals(vestingInformation.getCalculationFact()
								.getCreditingMethod())) {
			VestingCalculationFact fact = vestingInformation.getCalculationFact();
			if (fact != null
					&& fact.getEffectiveInput(VestingCalculationFact.PARAMETER_RAW_VESTED_YEARS_OF_SERVICE) != null) {
				errors.add(new ValidationError(PreviousYearsOfService,
						CensusErrorCodes.PreviousYearsOfServiceBeBlank));
			}
		}
*/
	}

	public void clear( HttpServletRequest request) {
	}

	/**
	 * Save the values submitted for comparison of changes for next save.
	 *
	 */
	public void saveInputValues() {
		previousValue = new HashMap<String, String>();
		for (String name : propertyMap.keySet()) {
			previousValue.put(name, getValue(name));
		}
	}

	/**
	 * Returns whether the first name, last name or ssn value has changed from
	 * last save
	 *
	 * @return
	 */
	public boolean isNameSsnChanged() {
		if (previousValue == null) {
			return true;
		}
		return isValueChanged(SSN) || isValueChanged(FirstName)
				|| isValueChanged(LastName);
	}

	/**
	 * Get the set of the name of field that has changed
	 *
	 * @return
	 */
	public Set<String> getChangedFromInitialValueFieldNames() {
		Set<String> changedSet = new HashSet<String>();
		EditEmployeeSnapshotForm cloned = (EditEmployeeSnapshotForm) getClonedForm();
		for (String name : propertyMap.keySet()) {
			if (cloned != null) {
				String origValue = cloned.getValue(name);
				String currentValue = getValue(name);
				if (!ObjectUtils.equals(origValue, currentValue)) {
					changedSet.add(name);
				}
			} else {
				changedSet.add(name);
			}
		}
		
		if (isEligibilityCalcCsfOn()) {

			List<EligibilityCalculationMoneyType> latest = getEligibilityServiceMoneyTypes();

			int index = 0;

			for (EligibilityCalculationMoneyType moneyType : latest) {
				EligibilityCalculationMoneyType latestMoneyType = latest
						.get(index);
				
				changedSet.add(latestMoneyType.getMoneyTypeId() + ":"
							+ "eligibityServiceMoneyTypeId[" + index
							+ "].eligibilityDate");
			
				index++;

			}
		}

		return changedSet;
	}

	/**
	 * Get the set of changed field names
	 *
	 * @return
	 */
	public Set<String> getChangedFieldNames() {
		Set<String> changedSet = new HashSet<String>();
		if (previousValue != null) {
			for (String name : propertyMap.keySet()) {
				if (isValueChanged(name)) {
					changedSet.add(name);
				}
			}
		} else {
			changedSet.addAll(propertyMap.keySet());
		}

		/*
		 * If one of address field changes, it is treated as all the address
		 * fields have changed.
		 */
		if (CollectionUtils.containsAny(changedSet, addressFieldSet)) {
			changedSet.addAll(addressFieldSet);
		}
		return changedSet;
	}

	/**
	 * If a value of a field changed
	 *
	 * @param name
	 * @return
	 */
	private boolean isValueChanged(String name) {
		String v = getValue(name);
		String pv = previousValue.get(name);
		return !ObjectUtils.equals(v, pv);
	}

	/**
	 * Check if current country selection is USA
	 *
	 * @return
	 */
	public boolean isCountryUSA() {
		String value = getValue(Country);
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
	 * Check if the similar ssn validation should be done It should be done in
	 * two cases: 1. acceptSimilarSsn = false 2. acceptSimilarSsn = true and
	 * Name/Ssn are changed
	 *
	 * @return true if no check similar ssn
	 */
	public boolean doNotCheckSimilarSsn() {
		if (acceptSimilarSsn) {
			if (isNameSsnChanged()) {
				acceptSimilarSsn = false;
			}
		}
		return acceptSimilarSsn;
	}

	public boolean isAcceptSimilarSsn() {
		return acceptSimilarSsn;
	}

	public void setAcceptSimilarSsn(boolean acceptSimilarSsn) {
		this.acceptSimilarSsn = acceptSimilarSsn;
	}

	public SSN getSsn() {
		return ssn;
	}

	public void setSsn(SSN ssn) {
		this.ssn = ssn;
	}

	public AddressStateCode getState() {
		return state;
	}

	public void setState(AddressStateCode state) {
		this.state = state;
	}

	public ZipCode getZipCode() {
		return zipCode;
	}

	public void setZipCode(ZipCode zipCode) {
		this.zipCode = zipCode;
	}

	public Collection getEligibleOptions() {
		return eligibleOptions;
	}
	
	public Collection getLongTermPartTimeAssessmentYearOptions() {
		return longTermPartTimeAssessmentYearOptions;
	}

	public Collection getEmploymentStatusOptions() {
		return employmentStatusOptions;
	}

	public Collection getCountryOptions() {
		return CensusLookups.getInstance().getCountries();
	}

	public Collection getNamePrefixOptions() {
		return CensusLookups.getInstance().getNamePrefix();
	}

	// ///////////////////// These are for properties to splitted in mulitiple
	// fields in page
	// //////////
	public class SSN implements Serializable {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		private String ssn1;

		private String ssn2;

		private String ssn3;

		public String getSsn1() {
			return ssn1;
		}

		public void setSsn1(String ssn1) {
			this.ssn1 = StringUtils.trim(ssn1);
		}

		public String getSsn2() {
			return ssn2;
		}

		public void setSsn2(String ssn2) {
			this.ssn2 = StringUtils.trim(ssn2);
		}

		public String getSsn3() {
			return ssn3;
		}

		public void setSsn3(String ssn3) {
			this.ssn3 = StringUtils.trim(ssn3);
		}

		protected String getValue() {
			if (ssn1 == null && ssn2 == null && ssn3 == null) {
				return "";
			} else {
				return (ssn1 == null ? "   " : ssn1)
						+ (ssn2 == null ? "  " : ssn2)
						+ (ssn3 == null ? "    " : ssn3);
			}
		}

		protected void setValue(String value) {
			ssn1 = null;
			ssn2 = null;
			ssn3 = null;
			if (value == null) {
				return;
			}
			int len = value.length();
			if (len > 0) {
				ssn1 = value.substring(0, len >= 3 ? 3 : len);
			}
			if (len > 3) {
				ssn2 = value.substring(3, len >= 5 ? 5 : len);
			}
			if (len > 5) {
				ssn3 = value.substring(5, len == 9 ? 9 : len);
			}
		}

		public void reset() {
			ssn1 = null;
			ssn2 = null;
			ssn3 = null;
		}
	}

	public class ZipCode implements Serializable {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		private String zipCode1;

		private String zipCode2;

		private String zipCodeNonUSA;

		public void reset() {
			zipCodeNonUSA = null;
			zipCode1 = null;
			zipCode2 = null;
		}

		public String getZipCode1() {
			return zipCode1;
		}

		public void setZipCode1(String zipCode1) {
			this.zipCode1 = StringUtils.trim(zipCode1);
		}

		public String getZipCode2() {
			return zipCode2;
		}

		public void setZipCode2(String zipCode2) {
			this.zipCode2 = StringUtils.trim(zipCode2);
		}

		public String getZipCodeNonUSA() {
			return zipCodeNonUSA;
		}

		public void setZipCodeNonUSA(String zipCodeNonUSA) {
			this.zipCodeNonUSA = StringUtils.trim(zipCodeNonUSA);
		}

		protected String getValue() {
			if (isCountryUSA()) {
				return (zipCode1 == null ? "" : zipCode1)
						+ (zipCode2 == null ? "" : zipCode2);
			} else {
				return zipCodeNonUSA;
			}
		}

		protected void setValue(String zipCode) {
			zipCode1 = null;
			zipCode2 = null;
			zipCodeNonUSA = null;
			if (isCountryUSA()) {
				if (zipCode == null) {
					return;
				}
				int len = zipCode.length();
				if (len > 0) {
					zipCode1 = zipCode.substring(0, len >= 5 ? 5 : len);
				}
				if (len > 5) {
					zipCode2 = zipCode.substring(5);
				}
			} else {
				zipCodeNonUSA = zipCode;
			}
		}
	}

	public class AddressStateCode implements Serializable {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		private String stateUSA;

		private String stateNonUSA;

		public void reset() {
			stateUSA = null;
			stateNonUSA = null;
		}

		public String getStateNonUSA() {
			return stateNonUSA;
		}

		public void setStateNonUSA(String stateNonUSA) {
			this.stateNonUSA = StringUtils.trim(stateNonUSA);
		}

		public String getStateUSA() {
			return stateUSA;
		}

		public void setStateUSA(String stateUSA) {
			this.stateUSA = StringUtils.trim(stateUSA);
		}

		protected String getValue() {
			if (isCountryUSA()) {
				return stateUSA;
			} else {
				return stateNonUSA;
			}
		}

		protected void setValue(String state) {
			if (isCountryUSA()) {
				stateUSA = state;
				stateNonUSA = null;
			} else {
				stateNonUSA = state;
				stateUSA = null;
			}
		}
	}

	static {
		propertyMap.put(FirstName, new StringPropertyEditor(
				"employeeDetailVO.firstName"));
		propertyMap.put(LastName, new StringPropertyEditor(
				"employeeDetailVO.lastName"));
		propertyMap.put(MiddleInitial, new StringPropertyEditor(
				"employeeDetailVO.middleInitial"));
		propertyMap.put(Prefix, new StringPropertyEditor(
				"employeeDetailVO.namePrefix"));
		propertyMap.put(EmployeeId, new StringPropertyEditor(
				"employeeDetailVO.employeeId"));
		propertyMap.put(SSN, new StringPropertyEditor(
				"employeeDetailVO.socialSecurityNumber"));
		propertyMap.put(BirthDate, new DatePropertyEditor(
				"employeeDetailVO.birthDate", DateFormat));

		propertyMap.put(HireDate, new DatePropertyEditor(
				"employeeDetailVO.hireDate", DateFormat));
		propertyMap.put(Division, new StringPropertyEditor(
				"employeeDetailVO.employerDivision"));
		propertyMap.put(EmploymentStatus, new StringPropertyEditor(
				"employeeDetailVO.employmentStatusCode"));
		propertyMap.put(EmploymentStatusEffectiveDate, new DatePropertyEditor(
				"employeeDetailVO.employmentStatusEffDate", DateFormat));
		propertyMap.put(AnnualBaseSalary, new CurrencyPropertyEditor(
				"employeeDetailVO.annualBaseSalary", 11));
		propertyMap.put(PlanYearToDateComp, new CurrencyPropertyEditor(
				"employeeVestingVO.yearToDateCompensation", 11));
		propertyMap.put(PlanParticipationDate, new DatePropertyEditor(
				"employeeDetailVO.planParticipationDate", DateFormat));
		propertyMap.put(MaskSensitiveInformation, new StringPropertyEditor(
				"employeeDetailVO.maskSensitiveInfoInd"));
		propertyMap.put(MaskSensitiveInformationComment,
				new StringPropertyEditor(
						"employeeDetailVO.maskSensitiveInfoComments"));
		propertyMap.put(YearToDatePlanHoursWorked, new IntegerPropertyEditor(
				"employeeVestingVO.yearToDateHrsWorked"));
		propertyMap.put(YearToDatePlanHoursEffDate, new DatePropertyEditor(
				"employeeVestingVO.yearToDateCompEffDt", DateFormat));

		propertyMap.put(AddressLine1, new StringPropertyEditor(
				"addressVO.addressLine1"));
		propertyMap.put(AddressLine2, new StringPropertyEditor(
				"addressVO.addressLine2"));
		propertyMap.put(City, new StringPropertyEditor("addressVO.city"));
		propertyMap.put(State, new StringPropertyEditor("addressVO.stateCode"));
		propertyMap.put(StateOfResidence, new StringPropertyEditor(
				"employeeDetailVO.residenceStateCode"));
		propertyMap.put(ZipCode, new StringPropertyEditor("addressVO.zipCode"));
		propertyMap.put(Country, new StringPropertyEditor("addressVO.country"));
		propertyMap.put("emailAddress", new StringPropertyEditor(
				"employeeDetailVO.employerProvidedEmailAddress"));

		propertyMap.put(EligibilityInd, new StringPropertyEditor(
				"employeeVestingVO.planEligibleInd"));
		propertyMap.put(EligibilityDate, new DatePropertyEditor(
				"employeeVestingVO.eligibilityDate", DateFormat));
		propertyMap.put(OptOut, new StringPropertyEditor(
				"employeeDetailVO.autoEnrollOptOutInd"));
		propertyMap.put(Ae90DaysOptOut, new StringPropertyEditor(
				"employeeDetailVO.ae90DaysOptOutInd"));
		propertyMap.put(DesignatedRothDefPer, new PercentagePropertyEditor(
				"employeeDetailVO.designatedRothDeferralPct", 10, 3));
		propertyMap.put(BeforeTaxDefPer, new PercentagePropertyEditor(
				"employeeDetailVO.beforeTaxDeferralPct", 10, 3));
		propertyMap.put(DesignatedRothDefAmt, new CurrencyPropertyEditor(
				"employeeDetailVO.designatedRothDeferralAmt", 8));
		propertyMap.put(BeforeTaxFlatDef, new CurrencyPropertyEditor(
				"employeeDetailVO.beforeTaxFlatDollarDeferral", 8));

		propertyMap.put(PreviousYearsOfService, new IntegerPropertyEditor(
				"employeeVestingVO.previousYrsOfService"));
		propertyMap
				.put(
						PreviousYearsOfServiceEffectiveDate,
						new DatePropertyEditor(
								"employeeVestingVO.previousYearsOfServiceEffectiveDate",
								DateFormat));

		propertyMap.put(FullyVestedInd, new StringPropertyEditor(
				"employeeVestingVO.fullyVestedInd"));
		propertyMap.put(FullyVestedIndEffectiveDate, new DatePropertyEditor(
				"employeeVestingVO.fullyVestedIndEffectiveDate", DateFormat));

		addressFieldSet.add(AddressLine1);
		addressFieldSet.add(AddressLine2);
		addressFieldSet.add(City);
		addressFieldSet.add(State);
		addressFieldSet.add(ZipCode);
		addressFieldSet.add(Country);
	}

	public List<LabelValueBean> getPreviousYearsOptions() {
		return previousYearsOptions;
	}

	public void setPreviousYearsOptions(
			List<LabelValueBean> previousYearsOptions) {
		this.previousYearsOptions = previousYearsOptions;
	}

	public EmployeeVestingInformation getVestingInformation() {
		return vestingInformation;
	}

	public void setVestingInformation(
			EmployeeVestingInformation vestingInformation) {
		this.vestingInformation = vestingInformation;
	}

	public List<LabelValueBean> getFullyVestedOverrideOptions() {
		return fullyVestedOverrideOptions;
	}

	public void setFullyVestedOverrideOptions(
			List<LabelValueBean> fullyVestedOverrideOptions) {
		this.fullyVestedOverrideOptions = fullyVestedOverrideOptions;
	}

	public boolean isDisableFullyVestedEffDate() {
		return disableFullyVestedEffDate;
	}

	public void setDisableFullyVestedEffDate(boolean disableFullyVestedEffDate) {
		this.disableFullyVestedEffDate = disableFullyVestedEffDate;
	}

	public boolean isDisablePrevioysYrsEffDate() {
		return disablePrevioysYrsEffDate;
	}

	public void setDisablePrevioysYrsEffDate(boolean disablePrevioysYrsEffDate) {
		this.disablePrevioysYrsEffDate = disablePrevioysYrsEffDate;
	}

	protected void onErrors(List<ValidationError> errors) {
		// disable the previous Years service effective date or fully vested eff
		// date
		setDisablePrevioysYrsEffDate(true);
		setDisableFullyVestedEffDate(true);
		for (Iterator<ValidationError> iter = errors.iterator(); iter.hasNext();) {
			ValidationError error = iter.next();
			List ids = error.getFieldIds();
			if (ids
					.contains(EmployeeSnapshotProperties.PreviousYearsOfServiceEffectiveDate)) {
				setDisablePrevioysYrsEffDate(false);
			} else if (ids
					.contains(EmployeeSnapshotProperties.FullyVestedIndEffectiveDate)) {
				setDisableFullyVestedEffDate(false);
			}
		}
	}
	
	  public Map<String,String> getValues() {
	        return values;
	    }

	public boolean isAe90DaysOptOutDisabled() {
		return ae90DaysOptOutDisabled;
	}

	public void setAe90DaysOptOutDisabled(boolean ae90DaysOptOutDisabled) {
		this.ae90DaysOptOutDisabled = ae90DaysOptOutDisabled;
	}

	public boolean isOptOutDisabled() {
		return optOutDisabled;
	}

	public void setOptOutDisabled(boolean optOutDisabled) {
		this.optOutDisabled = optOutDisabled;
	}
	  
	
	/**
	 * Check if opt out is disabled
	 *
	 * @return
	 */
	public boolean isOptOutDisabledForEmployee(Employee employee) throws SystemException {
		boolean disabled = true;
		
		//don't disable opt out if AE is not on
		if(employee.getEmployeeDetailVO() != null 
		   && employee.getEmployeeDetailVO().getContractId() != null 
		   && !isAutoEnrollmentOn(employee.getEmployeeDetailVO().getContractId())){
				return false;
		}
		
		if(employee.getEmployeeDetailVO() != null && "Y".equals(employee.getEmployeeDetailVO().getAutoEnrollOptOutInd())){
			disabled = false;
		}else if(employee.getParticipantContract() == null || "NT".equals(employee.getParticipantContract().getParticipantStatusCode())){
			disabled = false;
		}else{
			Integer contractId = employee.getParticipantContract().getContractId();
	        Long participantId = employee.getParticipantContract().getParticipantId();
			EmployeeServiceDelegate delegate = EmployeeServiceDelegate.getInstance(Constants.PS_APPLICATION_ID);  
			disabled = delegate.hasDeferralMoney(employee.getEmployeeDetailVO().getProfileId(),
                    contractId, participantId);
		}
		return disabled;
		
	}
	
	 private boolean isAutoEnrollmentOn(int contractId) throws SystemException {
	        try {
	            ContractServiceFeature csf = ContractServiceDelegate.getInstance()
	                    .getContractServiceFeature(contractId,
	                            ServiceFeatureConstants.AUTO_ENROLLMENT_FEATURE);
	            if (csf != null
	                    && ContractServiceFeature.internalToBoolean(csf.getValue()).booleanValue()) {
	                return true;
	            }
	            return false;
	        } catch (ApplicationException e) {
	            throw new SystemException(e, "Fail to get AE service feature");
	        }
	    }
	
		
	/**
	 * Check if opt out is disabled
	 *
	 * @return
	 */
	public boolean isAe90DaysWithdrawalDisabledForEmployee(Employee employee) throws SystemException {
		boolean disabled = true;
		
		if(employee.getParticipantContract() != null && employee.getParticipantContract().getParticipantId() > 0){
			Integer contractId = employee.getParticipantContract().getContractId();
		    Long participantId = employee.getParticipantContract().getParticipantId();
		    EmployeeServiceDelegate delegate = EmployeeServiceDelegate.getInstance(Constants.PS_APPLICATION_ID);  
			disabled = !delegate.hasDeferralMoney(employee.getEmployeeDetailVO().getProfileId(),
                    contractId, participantId);
		}
		
		return disabled;
		
	}
	
	/**
	 * Get the set of the name of field that has changed
	 *
	 * @return
	 */
	public Set<String> getEligibilityMoneyTypeFieldNames() {
		Set<String> changedSet = new HashSet<String>();
		
		if (isEligibilityCalcCsfOn()) {

			List<EligibilityCalculationMoneyType> latest = getEligibilityServiceMoneyTypes();

			int index = 0;

			for (EligibilityCalculationMoneyType moneyType : latest) {
				EligibilityCalculationMoneyType latestMoneyType = latest
						.get(index);
				
				changedSet.add(latestMoneyType.getMoneyTypeId() + ":"
							+ "eligibityServiceMoneyTypeId[" + index
							+ "].eligibilityDate");
			
				index++;

			}
		}

		return changedSet;
	}
	
	public String getSpecialPageType() {
        return specialPageType;
    }

    public void setSpecialPageType(String specialPageType) {
        this.specialPageType = specialPageType;
    }
    
    /**
	 * Struts uses this method.
	 */
    @Override 
	public void setEligibityServiceMoneyTypeId(int index,
			EligibilityCalculationMoneyType ecMoneyType) {

		getEligibilityServiceMoneyTypes().add(index, ecMoneyType);

	}

	/**
	 * Struts uses this method.
	 * @param index
	 * @return
	 */
    @Override
	public EligibilityCalculationMoneyType getEligibityServiceMoneyTypeId(
			int index) {

		return getEligibilityServiceMoneyTypes().get(index);

	}

	public List<LabelValueBean> getStateList() {
		return stateList;
	}

	public void setStateList(List<LabelValueBean> stateList) {
		this.stateList = stateList;
	}

	public List<LabelValueBean> getStateOfResidenceList() {
		return stateOfResidenceList;
	}

	public void setStateOfResidenceList(List<LabelValueBean> stateOfResidenceList) {
		this.stateOfResidenceList = stateOfResidenceList;
	}
    
    
 	
}