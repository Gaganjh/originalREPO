package com.manulife.pension.ps.web.login;

import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.passcode.RequestDetails;
import com.manulife.pension.service.security.valueobject.LoginPSValueObject;

interface UserPasswordValidation {
    
    LoginPSValueObject execute(String userName, String password, String company, RequestDetails details, String deviceToken)
    throws SecurityServiceException, SystemException;
    
    static UserPasswordValidation INSTANCE =
            new UserPasswordValidation() {
                
                @Override
                public LoginPSValueObject execute(
                        final String userName,
                        final String password,
                        final String company,
                        final RequestDetails details,
                        final String deviceToken)
                throws SecurityServiceException, SystemException {
                    
                    return SecurityServiceDelegate.getInstance().loginPS(userName, password, company, details, deviceToken, false);
                    
                }
                
            };
     
}
