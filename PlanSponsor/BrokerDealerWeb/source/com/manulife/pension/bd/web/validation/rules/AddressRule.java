package com.manulife.pension.bd.web.validation.rules;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.registration.util.AddressVO;
import com.manulife.pension.platform.web.validation.rules.generic.MandatoryRule;
import com.manulife.pension.platform.web.validation.rules.generic.RegularExpressionRule;
import com.manulife.pension.service.environment.EnvironmentServiceHelper;
import com.manulife.pension.validator.ValidationError;
import com.manulife.pension.validator.ValidationRule;

/**
 * This is a rule class to validate address.
 * 
 * @author Ilamparithi
 * 
 */
public class AddressRule extends ValidationRule {

    private static final AddressRule instance = new AddressRule();

    private static final MandatoryRule addLine1MandatoryRule = new MandatoryRule(
            BDErrorCodes.ADDRESS_LINE1_NOT_ENTERED);

    private static final MandatoryRule cityMandatoryRule = new MandatoryRule(
            BDErrorCodes.CITY_NOT_ENTERED);

    private static final MandatoryRule stateMandatoryRule = new MandatoryRule(
            BDErrorCodes.STATE_NOT_SELECTED_FOR_USA);

    private static final MandatoryRule zipCode1MandatoryRule = new MandatoryRule(
            BDErrorCodes.ZIP_CODE1_NOT_ENTERED_FOR_USA);

    private static final RegularExpressionRule invalidAddLine1RErule = new RegularExpressionRule(
            BDErrorCodes.ADDRESS_LINE1_INVALID, BDRuleConstants.INVALID_VALUE_RE);

    private static final RegularExpressionRule invalidAddLine2RErule = new RegularExpressionRule(
            BDErrorCodes.ADDRESS_LINE2_INVALID, BDRuleConstants.INVALID_VALUE_WITH_EMPTY_RE);

    private static final RegularExpressionRule invalidaCityRErule = new RegularExpressionRule(
            BDErrorCodes.CITY_INVALID, BDRuleConstants.INVALID_VALUE_RE);

    private static final RegularExpressionRule invalidaZipCode1RErule = new RegularExpressionRule(
            BDErrorCodes.ZIP_CODE1_INVALID_FOR_USA, BDRuleConstants.ZIPCODE1_FOR_USA_RE);

    private static final RegularExpressionRule invalidaZipCode2RErule = new RegularExpressionRule(
            BDErrorCodes.ZIP_CODE2_INVALID_FOR_USA, BDRuleConstants.ZIPCODE2_FOR_USA_RE);

    /**
     * Constructor
     */
    private AddressRule() {
        super(0);
    }

    /**
     * Returns an AddressRule instance
     * 
     * @return AddressRule
     */
    public static final AddressRule getInstance() {
        return instance;
    }

    /**
     * This method validates the address object and populates the Collection with errors if any.
     * 
     * @return boolean a boolean value to indicate whether the validation is passed or not
     */
    @SuppressWarnings("unchecked")
    public boolean validate(String[] fieldIds, Collection validationErrors, Object objectToValidate) {
        boolean isValid = false;
        if (objectToValidate != null && objectToValidate instanceof AddressVO) {
            AddressVO address = (AddressVO) objectToValidate;
            isValid = addLine1MandatoryRule.validate(fieldIds, validationErrors, address
                    .getAddress1());
            if (isValid) {
                isValid = invalidAddLine1RErule.validate(fieldIds, validationErrors, address
                        .getAddress1());
            }
            if (address.getAddress2() != null) {
                isValid = invalidAddLine2RErule.validate(fieldIds, validationErrors, address
                        .getAddress2());
            }
            isValid = cityMandatoryRule.validate(fieldIds, validationErrors, address.getCity());
            if (isValid) {
                isValid = invalidaCityRErule
                        .validate(fieldIds, validationErrors, address.getCity());
            }
            if (BDRuleConstants.USA_COUNTRY_CODE.equals(address.getCountry())) {
                boolean isStateValid = stateMandatoryRule.validate(fieldIds, validationErrors,
                        address.getUsState());
                isValid = zipCode1MandatoryRule.validate(fieldIds, validationErrors, address
                        .getUsZipCode1());
                if (isValid) {
                    isValid = invalidaZipCode1RErule.validate(fieldIds, validationErrors, address
                            .getUsZipCode1());
                    if (isValid) {
                        // Zip code 2 is optional
                        if (StringUtils.isEmpty(address.getUsZipCode2())) {
                            isValid = true;
                        } else {
                            isValid = invalidaZipCode2RErule.validate(fieldIds, validationErrors,
                                    address.getUsZipCode2());
                        }
                        if (isValid && isStateValid) {
                            isValid = EnvironmentServiceHelper.getInstance()
                                    .isUsZipCodeValidForState(address.getUsState(),
                                            address.getUsZipCode1());
                            if (!isValid) {
                                validationErrors.add(new ValidationError("UszipCode1",
                                        BDErrorCodes.ZIP_CODE_NOT_IN_RANGE_FOR_USA));
                            }
                        }
                    }
                }
            }

        }
        return isValid;
    }
}
