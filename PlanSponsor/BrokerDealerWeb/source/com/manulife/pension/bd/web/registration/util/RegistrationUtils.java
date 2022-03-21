package com.manulife.pension.bd.web.registration.util;

import com.manulife.pension.service.security.bd.valueobject.BDUserAddressValueObject;
import com.manulife.pension.service.security.bd.valueobject.PasswordChallenge;
import com.manulife.pension.service.security.bd.valueobject.UserSecurityValueObject;

/**
 * Helper class for registration
 * 
 * @author guweigu
 * 
 */
public class RegistrationUtils {

    /**
     * create and Populate the security value object from credential, challenge1, challenge2
     * 
     * @param credential
     * @param challenge1
     * @param challenge2
     * @return
     */
    public static UserSecurityValueObject getSecurityVO(UserCredential credential,
            PasswordChallengeInput challenge1, PasswordChallengeInput challenge2) {
        UserSecurityValueObject vo = new UserSecurityValueObject();
        populateSecurityVO(vo, credential, challenge1, challenge2);
        return vo;
    }

    /**
     * Populate the security value object from credential, challenge1, challenge2
     * 
     * @param vo
     * @param credential
     * @param challenge1
     * @param challenge2
     */
    public static void populateSecurityVO(UserSecurityValueObject vo, UserCredential credential,
            PasswordChallengeInput challenge1, PasswordChallengeInput challenge2) {

        vo.setUserName(credential.getUserId());
        vo.setPassword(credential.getPassword());
        PasswordChallenge[] challenges = new PasswordChallenge[] {
                new PasswordChallenge(challenge1.getQuestionText(), challenge1.getAnswer()),
                new PasswordChallenge(challenge2.getQuestionText(), challenge2.getAnswer()), };
        vo.setChallenges(challenges);
        return;
    }

    /**
     * This method populates the BDUserAddressValueObject from AddressVO
     * 
     * @param address
     * @return BDUserAddressValueObject
     */
    public static BDUserAddressValueObject populateBDAddressVO(AddressVO address) {
        BDUserAddressValueObject bdAddress = null;
        if (address != null) {
            bdAddress = new BDUserAddressValueObject();
            bdAddress.setAddrLine1(address.getAddress1());
            bdAddress.setAddrLine2(address.getAddress2());
            bdAddress.setCity(address.getCity());
            bdAddress.setState(address.getState());
            bdAddress.setCountry(address.getCountry());
            bdAddress.setZipCode(address.getZipCode());
        }
        return bdAddress;
    }
}
