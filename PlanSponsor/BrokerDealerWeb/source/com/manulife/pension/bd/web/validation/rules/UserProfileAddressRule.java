package com.manulife.pension.bd.web.validation.rules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import com.manulife.pension.platform.web.taglib.util.LabelValueBean;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.registration.util.AddressUtil;
import com.manulife.pension.bd.web.util.BDWebCommonUtils;
import com.manulife.pension.platform.web.validation.rules.generic.MandatoryRule;
import com.manulife.pension.platform.web.validation.rules.generic.RegularExpressionRule;
import com.manulife.pension.service.broker.valueobject.BrokerEntityAddress;
import com.manulife.pension.service.environment.EnvironmentServiceHelper;
import com.manulife.pension.validator.ValidationError;
import com.manulife.pension.validator.ValidationRule;

/**
 * This is a rule class to validate the address of User Profile.
 * 
 * @author Ilamparithi
 * 
 */
public class UserProfileAddressRule extends ValidationRule {
    
    private static final List<LabelValueBean> usStates = AddressUtil.getInstance().getStatesList();

    private static final MandatoryRule addLine1MandatoryRule = new MandatoryRule(
            BDErrorCodes.USER_PROFILE_ADDRESS_LINE1_NOT_ENTERED);

    private static final MandatoryRule cityMandatoryRule = new MandatoryRule(
            BDErrorCodes.USER_PROFILE_CITY_NOT_ENTERED);

    private static final MandatoryRule stateMandatoryRule = new MandatoryRule(
            BDErrorCodes.USER_PROFILE_STATE_NOT_ENTERED_FOR_US_ADDRESS);

    private static final MandatoryRule zipCodeMandatoryRule = new MandatoryRule(
            BDErrorCodes.USER_PROFILE_ZIP_CODE_NOT_ENTERED_FOR_US);

    private static final RegularExpressionRule invalidValueRErule = new RegularExpressionRule(
            BDErrorCodes.BROKER_PERSONAL_INFO_INVALID_VALUE, BDRuleConstants.BROKER_PERSONAL_INFO_INVALID_VALUE_RE);

    private static final RegularExpressionRule invalidValueREruleWithEmpty = new RegularExpressionRule(
            BDErrorCodes.BROKER_PERSONAL_INFO_INVALID_VALUE,
            BDRuleConstants.BROKER_PERSONAL_INFO_INVALID_VALUE_WITH_EMPTY_RE);

    private static final RegularExpressionRule invalidUSZipCodeRErule = new RegularExpressionRule(
            BDErrorCodes.USER_PROFILE_ZIP_CODE_INVALID_FOR_US, BDRuleConstants.US_ZIPCODE_RE);

    private static final String ADDRESS1_LABEL = "Mailing Address 1";

    private static final String ADDRESS2_LABEL = "Address 2";

    private static final String ADDRESS3_LABEL = "Address 3";

    private static final String CITY_LABEL = "City";

    private static final String STATE_LABEL = "State";

    private String ssnTaxId;

    private Object[] params;

    private String messageStr;

    /**
     * Constructor
     * 
     * @param ssnTaxId
     */
    public UserProfileAddressRule(String ssnTaxId) {
        super(0);
        this.ssnTaxId = ssnTaxId;
        if (StringUtils.isEmpty(ssnTaxId)) {
            messageStr = "";
        } else if (BDWebCommonUtils.isSsn(this.ssnTaxId)) {
            messageStr = BDConstants.SSN_MESSAGE_PREFIX
                    + BDWebCommonUtils.maskSsnTaxId(this.ssnTaxId);
        } else if (BDWebCommonUtils.isTaxId(this.ssnTaxId)) {
            messageStr = BDConstants.TAX_ID_MESSAGE_PREFIX
                    + BDWebCommonUtils.maskSsnTaxId(this.ssnTaxId);
        }
        params = new Object[] { messageStr };
    }

    /**
     * This method validates the BrokerEntityAddress object and populates the Collection with errors
     * if any.
     * 
     * @return boolean a boolean value to indicate whether the validation is passed or not
     */
    @SuppressWarnings("unchecked")
    public boolean validate(String[] fieldIds, Collection validationErrors, Object objectToValidate) {
        boolean isValid = false;
        if (objectToValidate != null && objectToValidate instanceof BrokerEntityAddress) {
            BrokerEntityAddress address = (BrokerEntityAddress) objectToValidate;
            isValid = addLine1MandatoryRule.validate(fieldIds, validationErrors, address
                    .getAddressLine1());
            setParams(isValid, validationErrors, params);
            if (isValid) {
                isValid = invalidValueRErule.validate(fieldIds, validationErrors, address
                        .getAddressLine1());
                setParams(isValid, validationErrors, new Object[] { messageStr, ADDRESS1_LABEL });
            }
            if (address.getAddressLine2() != null) {
                isValid = invalidValueREruleWithEmpty.validate(fieldIds, validationErrors,
                        address.getAddressLine2());
                setParams(isValid, validationErrors,
                        new Object[] { messageStr, ADDRESS2_LABEL });
            }
            if (address.getAddressLine3() != null) {
                isValid = invalidValueREruleWithEmpty.validate(fieldIds, validationErrors,
                        address.getAddressLine3());
                setParams(isValid, validationErrors,
                        new Object[] { messageStr, ADDRESS3_LABEL });
            }
            isValid = cityMandatoryRule.validate(fieldIds, validationErrors, address.getCity());
            setParams(isValid, validationErrors, params);
            if (isValid) {
                isValid = invalidValueRErule
                        .validate(fieldIds, validationErrors, address.getCity());
                setParams(isValid, validationErrors, new Object[] { messageStr, CITY_LABEL });
            }

            if (address.getUsaIndicator()) {
                boolean isStateValid = false;
                isValid = stateMandatoryRule.validate(fieldIds, validationErrors, address
                        .getState());
                setParams(isValid, validationErrors, params);
                if (isValid) {
                    isStateValid = isValidUSState(address.getState());
                    if (!isStateValid) {
                        validationErrors.add(new ValidationError(fieldIds,
                                BDErrorCodes.USER_PROFILE_STATE_INVALID_FOR_US_ADDRESS, params));
                    }
                }
                isValid = zipCodeMandatoryRule.validate(fieldIds, validationErrors, address
                        .getZipCode());
                setParams(isValid, validationErrors, params);
                if (isValid) {
                    isValid = invalidUSZipCodeRErule.validate(fieldIds, validationErrors, address
                            .getZipCode());
                    setParams(isValid, validationErrors, params);
                    if (isValid && isStateValid) {
                        isValid = EnvironmentServiceHelper.getInstance().isUsZipCodeValidForState(
                                address.getState(),
                                StringUtils.substring(address.getZipCode(), 0, 5));
                        if (!isValid) {
                            validationErrors
                                    .add(new ValidationError(
                                            "zipCode",
                                            BDErrorCodes.USER_PROFILE_ZIP_CODE_NOT_IN_RANGE_FOR_USA,
                                            params));
                        }
                    }
                }
            } else {
                if (!StringUtils.isEmpty(address.getState())) {
                    isValid = invalidValueRErule.validate(fieldIds, validationErrors, address
                            .getState());
                    setParams(isValid, validationErrors, new Object[] { messageStr, STATE_LABEL });
                }
            }

        }
        return isValid;
    }

    /**
     * This method takes out the last ValidationError object set in the Collection and sets the
     * message parameters of that object
     * 
     * @param isValid
     * @param validationErrors
     * @param params
     */
    private void setParams(boolean isValid, Collection<ValidationError> validationErrors,
            Object[] params) {
        if (!isValid) {
            ArrayList<ValidationError> errors = (ArrayList<ValidationError>) validationErrors;
            errors.get(validationErrors.size() - 1).setParams(params);
            validationErrors = errors;
        }
    }

    /**
     * This method tests whether the state entered is a valid US State
     * 
     * @param state
     * @return
     */
    private boolean isValidUSState(String state) {
        boolean isValid = false;
        for (LabelValueBean stateBean : usStates) {
            if (StringUtils.equalsIgnoreCase(state, stateBean.getValue())) {
                isValid = true;
                break;
            }
        }
        return isValid;
    }
}
