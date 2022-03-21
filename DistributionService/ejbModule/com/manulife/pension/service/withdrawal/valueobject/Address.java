/*
 * Address.java,v 1.2 2006/09/25 19:14:26 Paul_Glenn Exp
 * Address.java,v
 * Revision 1.2  2006/09/25 19:14:26  Paul_Glenn
 * Format.
 *
 * Revision 1.1  2006/08/14 18:41:18  Paul_Glenn
 * Refactor value objects.
 *
 */
package com.manulife.pension.service.withdrawal.valueobject;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.TransformerUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.NestableRuntimeException;

import com.manulife.pension.service.distribution.valueobject.DistributionAddress;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessage;

/**
 * Address is the data transfer object (DTO, or value object) for address data.
 * 
 * @author Paul_Glenn
 * @author glennpa
 * @version 1.2 2006/09/25 19:14:26
 */
public class Address extends BaseWithdrawal implements DistributionAddress {

    /**
     * FIELDS_TO_EXCLUDE_FROM_LOGGING.
     */
    private static final String[] FIELDS_TO_EXCLUDE_FROM_LOGGING = { "nonMatchedCountryName" };

    /**
     * Constant that defines the country code for USA.
     */
    public static final String USA_COUNTRY_CODE = "USA";

    // Properties that do not clone properly
    private static final String BLANK_PROPERTY = "blank";

    private static final String ERROR_CODES_PROPERTY = "errorCodes";

    private static final String WARNING_CODES_PROPERTY = "warningCodes";

    private static final String ALERT_CODES_PROPERTY = "alertCodes";

    /**
     * Default serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    private String addressLine1;

    private String addressLine2;

    private String city;

    private String stateCode;

    private String zipCode1;

    private String zipCode2;

    // Default to USA Country Code
    private String countryCode = USA_COUNTRY_CODE;

    private Integer recipientNo;

    private Integer payeeNo;

    private String distributionTypeCode;

    private String nonMatchedCountryName;
    
	private String createdUserIdType; 
	
    /**
     * @return the addressLine1
     */
    public String getAddressLine1() {
        return addressLine1;
    }

    /**
     * @TODO Should be replaced by an AddressUi object.
     * @return the escaped addressLine1
     */
    public String getEscapedAddressLine1() {
        return StringEscapeUtils.escapeJavaScript(addressLine1);
    }

    /**
     * @param addressLine1 the addressLine1 to set
     */
    public void setAddressLine1(final String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    /**
     * @return the addressLine2
     */
    public String getAddressLine2() {
        return addressLine2;
    }

    /**
     * @TODO Should be replaced by an AddressUi object.
     * @return the escaped addressLine2
     */
    public String getEscapedAddressLine2() {
        return StringEscapeUtils.escapeJavaScript(addressLine2);
    }

    /**
     * @param addressLine2 the addressLine2 to set
     */
    public void setAddressLine2(final String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @TODO Should be replaced by an AddressUi object.
     * @return the escaped city
     */
    public String getEscapedCity() {
        return StringEscapeUtils.escapeJavaScript(city);
    }

    /**
     * @param city the city to set
     */
    public void setCity(final String city) {
        this.city = city;
    }

    /**
     * @return the countryCode
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * @param countryCode the countryCode to set
     */
    public void setCountryCode(final String countryCode) {
        this.countryCode = countryCode;
    }

    /**
     * @return the stateCode
     */
    public String getStateCode() {
        return stateCode;
    }

    /**
     * @param stateCode the stateCode to set
     */
    public void setStateCode(final String stateCode) {
        this.stateCode = stateCode;
    }

    /**
     * @return the zipCode
     */
    public String getZipCode() {
        return new StringBuffer(StringUtils.defaultString(zipCode1)).append(
                StringUtils.defaultString(zipCode2)).toString();
    }

    /**
     * @param zipCode the zipCode to set
     */
    public void setZipCode(final String zipCode) {

        final String nonNullZip = StringUtils.defaultString(zipCode);
        this.zipCode1 = StringUtils.substring(nonNullZip, 0, ZIP_FIRST_LENGTH);
        this.zipCode2 = StringUtils.substring(nonNullZip, ZIP_FIRST_LENGTH, ZIP_CODE_LENGTH);
    }

    /**
     * @return the zipCode1
     */
    public String getZipCode1() {
        return zipCode1;
    }

    /**
     * @param zipCode1 the zipCode1 to set
     */
    public void setZipCode1(final String zipCode1) {
        this.zipCode1 = zipCode1;
    }

    /**
     * @return the zipCode2
     */
    public String getZipCode2() {
        return zipCode2;
    }

    /**
     * @param zipCode2 the zipCode2 to set
     */
    public void setZipCode2(final String zipCode2) {
        this.zipCode2 = zipCode2;
    }

    /**
     * @return the distrubution type code
     */
    public String getDistributionTypeCode() {
        return distributionTypeCode;
    }

    /**
     * @param distributionTypeCode the distribution type code
     */
    public void setDistributionTypeCode(final String distributionTypeCode) {
        this.distributionTypeCode = distributionTypeCode;
    }

    /**
     * @return the payee number
     */
    public Integer getPayeeNo() {
        return payeeNo;
    }

    /**
     * @param payeeNo the payee number
     */
    public void setPayeeNo(final Integer payeeNo) {
        this.payeeNo = payeeNo;
    }

    /**
     * @return the recipient number
     */
    public Integer getRecipientNo() {
        return recipientNo;
    }

    /**
     * @param recipientNo the recipient number
     */
    public void setRecipientNo(final Integer recipientNo) {
        this.recipientNo = recipientNo;
    }

    /**
     * 
     * @return the non-matched country name
     */
    public String getNonMatchedCountryName() {
        return nonMatchedCountryName;
    }

    /**
     * 
     * @param nonMatchedCountryName the non-matched country name
     */
    public void setNonMatchedCountryName(String nonMatchedCountryName) {
        this.nonMatchedCountryName = nonMatchedCountryName;
    }

    
    /**
	 * @return the createdUserIdType
	 */
	public String getCreatedUserIdType() {
		return createdUserIdType;
	}

	/**
	 * @param createdUserIdType the createdUserIdType to set
	 */
	public void setCreatedUserIdType(String createdUserIdType) {
		this.createdUserIdType = createdUserIdType;
	}

	/**
     * Checks if the default object has been inititalized or not.
     * 
     * @return boolean - True if the object is still blank.
     */
    public boolean isBlank() {
        boolean result = true;

        result &= StringUtils.isBlank(getAddressLine1());
        result &= StringUtils.isBlank(getAddressLine2());
        result &= StringUtils.isBlank(getCity());
        result &= StringUtils.isBlank(getStateCode());
        result &= StringUtils.isBlank(getZipCode1());
        result &= StringUtils.isBlank(getZipCode2());
        result &= (StringUtils.isBlank(getCountryCode()) || StringUtils.equals(USA_COUNTRY_CODE,
                getCountryCode()));
        result &= StringUtils.isBlank(getDistributionTypeCode());
        result &= (getRecipientNo() == null);
        result &= (getPayeeNo() == null);
        result &= (getSubmissionId() == null);
        result &= (getCreated() == null);
        result &= (getLastUpdated() == null);

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean doAlertCodesExist() {
        return CollectionUtils.isNotEmpty(getAlertCodes());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean doErrorCodesExist() {
        return CollectionUtils.isNotEmpty(getErrorCodes());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean doWarningCodesExist() {
        return CollectionUtils.isNotEmpty(getWarningCodes());
    }

    /**
     * Override of the base clone method due to problems with BeanUtils and the access method
     * isBlank().
     * 
     * @see com.manulife.pension.service.withdrawal.valueobject.BaseWithdrawal#clone()
     */
    @Override
    public Object clone() {

        try {
            final Map properties = BeanUtils.describe(this);
            properties.remove(BLANK_PROPERTY);
            properties.remove(ERROR_CODES_PROPERTY);
            properties.remove(WARNING_CODES_PROPERTY);
            properties.remove(ALERT_CODES_PROPERTY);

            final Address address = (Address) super.clone();
            address.setErrorCodes((Collection<WithdrawalMessage>) TransformerUtils
                    .cloneTransformer().transform(getErrorCodes()));
            address.setWarningCodes((Collection<WithdrawalMessage>) TransformerUtils
                    .cloneTransformer().transform(getWarningCodes()));
            address.setAlertCodes((Collection<WithdrawalMessage>) TransformerUtils
                    .cloneTransformer().transform(getAlertCodes()));
            BeanUtils.populate(address, properties);
            return address;

        } catch (IllegalAccessException e) {
            throw new NestableRuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new NestableRuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new NestableRuntimeException(e);
        }
    }

    /**
     * Clones only the address data portions of the object.
     */
    public Address cloneAddress() {
        final Address address = new Address();
        address.setAddressLine1(this.getAddressLine1());
        address.setAddressLine2(this.getAddressLine2());
        address.setCity(this.getCity());
        address.setZipCode(this.getZipCode());
        address.setStateCode(this.getStateCode());
        address.setCountryCode(this.getCountryCode());
        address.setDistributionTypeCode(this.getDistributionTypeCode());
        address.setPayeeNo(this.getPayeeNo());
        address.setRecipientNo(this.getRecipientNo());
        
        // Added for CR 5.
        //TODO : do we need to clone this
        //address.setCreatedUserIdType(this.getCreatedUserIdType());
        // End for CR 5.
        return address;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<String> fieldNamesToExcludeFromLogging() {

        final Collection<String> toExclude = new ArrayList<String>(
                FIELDS_TO_EXCLUDE_FROM_LOGGING.length);

        CollectionUtils.addAll(toExclude, FIELDS_TO_EXCLUDE_FROM_LOGGING);

        toExclude.addAll(super.fieldNamesToExcludeFromLogging());

        return toExclude;
    }

}
