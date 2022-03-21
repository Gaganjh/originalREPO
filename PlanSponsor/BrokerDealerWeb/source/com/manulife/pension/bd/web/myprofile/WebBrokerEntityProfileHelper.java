package com.manulife.pension.bd.web.myprofile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.util.BDWebCommonUtils;
import com.manulife.pension.bd.web.validation.rules.BDRuleConstants;
import com.manulife.pension.bd.web.validation.rules.UserProfileAddressRule;
import com.manulife.pension.platform.web.validation.rules.generic.RegularExpressionRule;
import com.manulife.pension.service.broker.valueobject.BrokerEntityExtendedProfile;
import com.manulife.pension.service.broker.valueobject.impl.BrokerEntityExtendedProfileImpl;
import com.manulife.pension.service.security.bd.valueobject.BrokerEntityAssoc;
import com.manulife.pension.service.security.bd.valueobject.ExtendedBrokerUserProfile;
import com.manulife.pension.validator.ValidationError;

/**
 * The helper class for web broker entity profile
 * 
 * @author guweigu
 * 
 */
public class WebBrokerEntityProfileHelper {
    
    private static final Logger logger = Logger.getLogger(WebBrokerEntityProfileHelper.class);

    private final static RegularExpressionRule invalidContactEmailRERule = new RegularExpressionRule(
            BDErrorCodes.PERSONAL_INFO_EMAIL_NOT_ENTERED, BDRuleConstants.EMAIL_ADDRESS_RE);

    private final static RegularExpressionRule invalidTelephoneNumberRERule = new RegularExpressionRule(
            BDErrorCodes.PERSONAL_INFO_TELEPHONE_NUMBER_INVALID,
            BDRuleConstants.TELEPHONE_MOBILE_FAX_NUMBER_RE);

    private final static RegularExpressionRule invalidMobileNumberRERule = new RegularExpressionRule(
            BDErrorCodes.PERSONAL_INFO_MOBILE_NUMBER_INVALID,
            BDRuleConstants.TELEPHONE_MOBILE_FAX_NUMBER_RE);

    private final static RegularExpressionRule invalidFaxNumberRERule = new RegularExpressionRule(
            BDErrorCodes.PERSONAL_INFO_FAX_NUMBER_INVALID,
            BDRuleConstants.TELEPHONE_MOBILE_FAX_NUMBER_RE);

    private static final RegularExpressionRule invalidValueRErule = new RegularExpressionRule(
            BDErrorCodes.BROKER_PERSONAL_INFO_INVALID_VALUE, BDRuleConstants.BROKER_PERSONAL_INFO_INVALID_VALUE_RE);
    
    private static final RegularExpressionRule invalidFirstLastNameRErule = new RegularExpressionRule(
            BDErrorCodes.INVALID_PROFILE_FIRST_AND_LAST_NAME, BDRuleConstants.VALIDATE_FIRST_NAME_AND_LAST_NAME_RE);

    
    private static final String FIRST_NAME_LABEL = "First Name";

    private static final String MIDDLE_INITIAL_LABEL = "Middle Initial";

    private static final String LAST_NAME_LABEL = "Last Name";

    private static final String COMPANY_NAME_LABEL = "Company Name";
    
    /**
     * Create the WebBRokerEntityProfile list
     * 
     * @param brokerUserProfile
     * @return
     */
    public static List<WebBrokerEntityProfile> createWebBrokerEntityList(
            ExtendedBrokerUserProfile brokerUserProfile) {
        List<WebBrokerEntityProfile> brokerEntityProfilesList = null;
        if (brokerUserProfile != null) {
            List<BrokerEntityAssoc> activeBrokerEntities = brokerUserProfile
                    .getActiveBrokerEntities();
            if (activeBrokerEntities != null) {
                brokerEntityProfilesList = new ArrayList<WebBrokerEntityProfile>(brokerUserProfile
                        .getActiveBrokerEntities().size());
                // Adding broker entity profiles to the list
                Iterator<BrokerEntityAssoc> it = activeBrokerEntities.iterator();
                while (it.hasNext()) {
                    brokerEntityProfilesList.add(new WebBrokerEntityProfile(
                            (BrokerEntityExtendedProfileImpl) it.next().getBrokerEntity()));
                }
            }
        }
        return brokerEntityProfilesList;
    }

    /**
     * Convert the web version back to the value object
     * 
     * @param list
     * @return
     */
    public static List<BrokerEntityExtendedProfile> getBrokerExtendedProfile(
            List<WebBrokerEntityProfile> list) {
        List<BrokerEntityExtendedProfile> profiles = new ArrayList<BrokerEntityExtendedProfile>(
                list.size());
        for (WebBrokerEntityProfile p : list) {
            profiles.add(p.getDelegate());
        }
        return profiles;
    }

    /**
     * This method will sort the BrokerEntityProfile objects. A profile with SSN will come first
     * followed by the profiles with Tax Id. (Profiles with Tax Id will be ordered in ascending
     * order)
     * 
     * @param brokerEntityProfilesList
     * @return List a List of WebBrokerEntityProfile objects
     */
    public static List<WebBrokerEntityProfile> sortBrokerEntityProfiles(
            List<WebBrokerEntityProfile> brokerEntityProfilesList) {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> sortBrokerEntityProfiles");
        }
        List<WebBrokerEntityProfile> sortedBrokerEntityProfilesList = new ArrayList<WebBrokerEntityProfile>(
                brokerEntityProfilesList.size());

        List<WebBrokerEntityProfile> taxIdBrokerEntityProfilesList = new ArrayList<WebBrokerEntityProfile>();
        Iterator<WebBrokerEntityProfile> profilesIter = brokerEntityProfilesList.iterator();
        while (profilesIter.hasNext()) {
            WebBrokerEntityProfile entityProfile = profilesIter.next();
            if (BDWebCommonUtils.isSsn(entityProfile.getSsnTaxId())) {
                sortedBrokerEntityProfilesList.add(entityProfile);
            } else if (BDWebCommonUtils.isTaxId(entityProfile.getSsnTaxId())) {
                taxIdBrokerEntityProfilesList.add(entityProfile);
            }
        }
        Collections.sort(taxIdBrokerEntityProfilesList, new Comparator<WebBrokerEntityProfile>() {
            public int compare(WebBrokerEntityProfile o1, WebBrokerEntityProfile o2) {
                return o1.getSsnTaxId().compareTo(o2.getSsnTaxId());
            }
        });
        if (taxIdBrokerEntityProfilesList.size() > 0) {
            sortedBrokerEntityProfilesList.addAll(taxIdBrokerEntityProfilesList);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> sortBrokerEntityProfiles");
        }
        return sortedBrokerEntityProfilesList;
    }

    /**
     * This is a validate method to validate the address object of external user profile
     * 
     * @param errors
     * @param brokerEntityProfilesList
     */
    public static void validateBrokerEntityProfile(List<ValidationError> errors,
            List<WebBrokerEntityProfile> brokerEntityProfilesList, HashMap<String, String> addressFlagMap) {
        UserProfileAddressRule addressRule = null;
        String messageStr = null;
        boolean isSingle = (brokerEntityProfilesList.size() == 1);
        for (WebBrokerEntityProfile profile : brokerEntityProfilesList) {
            if (isSingle) {
                messageStr = "";
            } else if (profile.isSsn()) {
                messageStr = BDConstants.SSN_MESSAGE_PREFIX
                        + BDWebCommonUtils.maskSsnTaxId(profile.getSsnTaxId());
            } else {
                messageStr = BDConstants.TAX_ID_MESSAGE_PREFIX
                        + BDWebCommonUtils.maskSsnTaxId(profile.getSsnTaxId());
            }
            Object[] params = new Object[] { messageStr };
            List<ValidationError> tempArrayList = new ArrayList<ValidationError>(1);
            if (profile.isShowPersonalName()) {

                if (StringUtils.isEmpty(profile.getFirstName())) {
                    errors.add(new ValidationError(
                            MyProfileBrokerPersonalInfoForm.FIELD_CONTACT_FIRST_NAME,
                            BDErrorCodes.PERSONAL_INFO_FIRST_NAME_NOT_ENTERED, params));
                } else if (!invalidFirstLastNameRErule.validate(
                        MyProfileBrokerPersonalInfoForm.FIELD_CONTACT_FIRST_NAME, tempArrayList,
                        profile.getFirstName())) {
                    errors.add(new ValidationError(
                            MyProfileBrokerPersonalInfoForm.FIELD_CONTACT_FIRST_NAME,
                            BDErrorCodes.INVALID_PROFILE_FIRST_AND_LAST_NAME, new Object[] {FIRST_NAME_LABEL }));
                    tempArrayList.clear();
                }
                if (StringUtils.isNotEmpty(profile.getMiddleInit())) {
                    if (!invalidFirstLastNameRErule.validate(
                            MyProfileBrokerPersonalInfoForm.FIELD_CONTACT_MIDDLE_INITIAL,
                            tempArrayList, profile.getMiddleInit())) {
                        errors.add(new ValidationError(
                                MyProfileBrokerPersonalInfoForm.FIELD_CONTACT_MIDDLE_INITIAL,
                                BDErrorCodes.INVALID_PROFILE_FIRST_AND_LAST_NAME, new Object[] { MIDDLE_INITIAL_LABEL }));
                        tempArrayList.clear();
                    }
                }
                if (StringUtils.isEmpty(profile.getLastName())) {
                    errors.add(new ValidationError(
                            MyProfileBrokerPersonalInfoForm.FIELD_CONTACT_LAST_NAME,
                            BDErrorCodes.PERSONAL_INFO_LAST_NAME_NOT_ENTERED, params));
                } else if (!invalidFirstLastNameRErule.validate(
                        MyProfileBrokerPersonalInfoForm.FIELD_CONTACT_LAST_NAME, tempArrayList,
                        profile.getLastName())) {
                    errors.add(new ValidationError(
                            MyProfileBrokerPersonalInfoForm.FIELD_CONTACT_LAST_NAME,
                            BDErrorCodes.INVALID_PROFILE_FIRST_AND_LAST_NAME, new Object[] {LAST_NAME_LABEL }));
                    tempArrayList.clear();
                }
            }
            if (profile.isShowOrgName()) {
                if (StringUtils.isEmpty(profile.getOrgName())) {
                    errors.add(new ValidationError(
                            MyProfileBrokerPersonalInfoForm.FIELD_CONTACT_COMPANY_NAME,
                            BDErrorCodes.PERSONAL_INFO_COMPANY_NAME_NOT_ENTERED, new Object[] {
                                    messageStr, COMPANY_NAME_LABEL }));
                } else if (!invalidValueRErule.validate(
                        MyProfileBrokerPersonalInfoForm.FIELD_CONTACT_COMPANY_NAME, tempArrayList,
                        profile.getOrgName())) {
                    errors.add(new ValidationError(
                            MyProfileBrokerPersonalInfoForm.FIELD_CONTACT_COMPANY_NAME,
                            BDErrorCodes.BROKER_PERSONAL_INFO_INVALID_VALUE, new Object[] {
                                    messageStr, COMPANY_NAME_LABEL }));
                    tempArrayList.clear();
                }
            }
            
            if (StringUtils.isEmpty(profile.getEmailAddress())) {
                errors.add(new ValidationError(MyProfileBrokerPersonalInfoForm.FIELD_CONTACT_EMAIL,
                        BDErrorCodes.PERSONAL_INFO_EMAIL_NOT_ENTERED, params));
            } else if (!invalidContactEmailRERule.validate(
                    MyProfileBrokerPersonalInfoForm.FIELD_CONTACT_EMAIL, tempArrayList, profile
                            .getEmailAddress())) {
                errors.add(new ValidationError(MyProfileBrokerPersonalInfoForm.FIELD_CONTACT_EMAIL,
                        BDErrorCodes.PERSONAL_INFO_EMAIL_INVALID, params));
                tempArrayList.clear();
            }
            addressRule = new UserProfileAddressRule(isSingle ? "" : profile.getSsnTaxId());
            if (StringUtils.equals(addressFlagMap.get(profile.getSsnTaxId()),BDConstants.MAILING_ADDRESS)) {
                addressRule.validate(MyProfileBrokerPersonalInfoForm.FIELD_ADDRESS, errors, profile
                        .getMailingAddress());
            } else {
                addressRule.validate(MyProfileBrokerPersonalInfoForm.FIELD_ADDRESS, errors, profile
                        .getHomeAddress());
            }
            if (!invalidTelephoneNumberRERule.validate(
                    MyProfileBrokerPersonalInfoForm.FIELD_TELEPHONE_NUMBER, tempArrayList, profile
                            .getPhoneNum())) {
                errors.add(new ValidationError(
                        MyProfileBrokerPersonalInfoForm.FIELD_TELEPHONE_NUMBER,
                        BDErrorCodes.PERSONAL_INFO_TELEPHONE_NUMBER_INVALID, params));
                tempArrayList.clear();
            }
            if (!invalidMobileNumberRERule.validate(
                    MyProfileBrokerPersonalInfoForm.FIELD_MOBILE_NUMBER, tempArrayList, profile
                            .getCellPhoneNum())) {
                errors.add(new ValidationError(MyProfileBrokerPersonalInfoForm.FIELD_MOBILE_NUMBER,
                        BDErrorCodes.PERSONAL_INFO_MOBILE_NUMBER_INVALID, params));
                tempArrayList.clear();
            }
            if (!invalidFaxNumberRERule.validate(MyProfileBrokerPersonalInfoForm.FIELD_FAX_NUMBER,
                    tempArrayList, profile.getFaxNum())) {
                errors.add(new ValidationError(MyProfileBrokerPersonalInfoForm.FIELD_FAX_NUMBER,
                        BDErrorCodes.PERSONAL_INFO_FAX_NUMBER_INVALID, params));
                tempArrayList.clear();
            }
        }
    }

    /**
     * This method receives a List of WebBrokerEntityProfile objects and returns a deep copy of that
     * list
     * 
     * @param brokerEntityProfilesList
     * @return List<WebBrokerEntityProfile>
     */
    public static List<WebBrokerEntityProfile> copyBrokerEntityProfilesList(
            List<WebBrokerEntityProfile> brokerEntityProfilesList) {
        List<WebBrokerEntityProfile> newList = new ArrayList<WebBrokerEntityProfile>(
                brokerEntityProfilesList.size());
        for (WebBrokerEntityProfile profile : brokerEntityProfilesList) {
            BrokerEntityExtendedProfileImpl profileImpl = (BrokerEntityExtendedProfileImpl) profile
                    .getDelegate();
            newList.add(new WebBrokerEntityProfile(profileImpl.copy()));
        }
        return newList;
    }
    
    /**
     * This method returns a map of address flags for each profile entity. The flag will indicate whether
     * mailing address is used or home address is used.
     * 
     * @param brokerEntityProfilesList
     * @return HashMap
     */
    public static HashMap<String, String> getAddressFlagMap(
            List<WebBrokerEntityProfile> brokerEntityProfilesList) {
        HashMap<String, String> addressFlagMap = new HashMap<String, String>(
                brokerEntityProfilesList.size());
        for (WebBrokerEntityProfile profile : brokerEntityProfilesList) {
            addressFlagMap.put(profile.getSsnTaxId(),
                    (profile.getMailingAddress().isBlank()) ? BDConstants.HOME_ADDRESS : BDConstants.MAILING_ADDRESS);
        }
        return addressFlagMap;
    }
}
