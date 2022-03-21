package com.manulife.pension.bd.web.registration;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.bd.web.registration.util.Ssn;
import com.manulife.pension.bd.web.registration.util.TaxId;
import com.manulife.pension.platform.web.controller.BaseForm;

/**
 * This is the form bean for step 1 of Broker Registration
 * 
 * @author Ilamparithi
 * 
 */
public class RegisterBrokerValidationForm extends BaseForm {

    private static final long serialVersionUID = 1L;

    public static final String FIELD_FIRST_NAME = "firstName";

    public static final String FIELD_LAST_NAME = "lastName";

    public static final String FIELD_EMAIL_ADDRESS = "emailAddress";

    public static final String FIELD_CONTRACT_NUMBER = "contractNumber";

    public static final String FIELD_SSN = "ssn";

    public static final String FIELD_TAX_ID = "taxId";

    private String userHasContract = "Yes";

    private String firstName;

    private String lastName;

    private String emailAddress;

    private String contractNumber;

    private Integer contractNumValue = null;

    private Ssn ssn = new Ssn();

    private TaxId taxId = new TaxId();

    private boolean changed = false;

    /**
     * Returns Yes/No to indicate whether the user has any contract
     * 
     * @return the userHasContract
     */
    public String getUserHasContract() {
        return userHasContract;
    }

    /**
     * Sets the userHasContract with Yes/No values
     * 
     * @param userHasContract the userHasContract to set
     */
    public void setUserHasContract(String userHasContract) {
        this.userHasContract = userHasContract;
    }

    /**
     * Returns the firstName
     * 
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the firstName
     * 
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = StringUtils.trimToEmpty(firstName);
    }

    /**
     * Returns the lastName
     * 
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the lastName
     * 
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = StringUtils.trimToEmpty(lastName);
    }

    /**
     * Returns the emailAddress
     * 
     * @return the emailAddress
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Sets the emailAddress
     * 
     * @param emailAddress the emailAddress to set
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = StringUtils.trimToEmpty(emailAddress);
    }

    /**
     * Returns the SSN
     * 
     * @return the ssn
     */
    public Ssn getSsn() {
        return ssn;
    }

    /**
     * Sets the ssn
     * 
     * @param ssn the ssn to set
     */
    public void setSsn(Ssn ssn) {
        this.ssn = ssn;
    }

    /**
     * Returns the taxId
     * 
     * @return the taxId
     */
    public TaxId getTaxId() {
        return taxId;
    }

    /**
     * Sets the taxId
     * 
     * @param taxId the taxId to set
     */
    public void setTaxId(TaxId taxId) {
        this.taxId = taxId;
    }

    /**
     * Returns the contract number
     * 
     * @return String
     */
    public String getContractNumber() {
        return contractNumber;
    }

    /**
     * Sets the contractNumber and also sets the contractNumValue
     * 
     * @param contractNumber
     */
    public void setContractNumber(String contractNumber) {
        this.contractNumber = StringUtils.trimToEmpty(contractNumber);
        if (!StringUtils.isEmpty(this.contractNumber)) {
            try {
                contractNumValue = Integer.parseInt(contractNumber);
            } catch (NumberFormatException e) {
                contractNumValue = null;
            }
        }
    }

    /**
     * Returns the contractNumValue
     * 
     * @return Integer
     */
    public Integer getContractNumValue() {
        return contractNumValue;
    }

    /**
     * Returns a flag that indicates whether a form is dirty or not
     * 
     * @return the changed
     */
    public boolean isChanged() {
        return changed;
    }

    /**
     * Sets the dirty flag of the form
     * 
     * @param changed the changed to set
     */
    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    /**
     * Copy the data from one for to another
     * 
     * @param src
     */
    public void copyFrom(RegisterBrokerValidationForm src) {
        this.userHasContract = src.getUserHasContract();
        this.firstName = src.getFirstName();
        this.lastName = src.getLastName();
        this.emailAddress = src.getEmailAddress();
        this.contractNumber = src.getContractNumber();
        this.ssn.copyFrom(src.getSsn());
        this.taxId.copyFrom(src.getTaxId());
        this.changed = src.isChanged();
    }
}
