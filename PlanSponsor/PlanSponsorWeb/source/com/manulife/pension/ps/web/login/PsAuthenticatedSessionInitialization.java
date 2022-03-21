package com.manulife.pension.ps.web.login;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.passcode.AuthenticatedSessionInitialization;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.home.SelectContractDetailUtil;
import com.manulife.pension.ps.web.home.SessionBindingListener;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.security.role.ExternalClientUser;
import com.manulife.pension.service.security.role.ThirdPartyAdministrator;
import com.manulife.pension.service.security.utility.SecurityConstants;
import com.manulife.pension.service.security.valueobject.LoginPSValueObject;

enum PsAuthenticatedSessionInitialization
implements AuthenticatedSessionInitialization<LoginPSValueObject> {
    
    INSTANCE;
    
    @Override
    public void execute(
            final String userName,
            final HttpServletRequest request,
            final HttpServletResponse response,
            final LoginPSValueObject loginVo)
    throws SystemException {
        
        final UserProfile user = createUserProfile(loginVo, userName);
        
        request.getSession(true).setAttribute(Constants.USERPROFILE_KEY, user);

        //SessionMonitor.getSessionMonitor().addSession(request.getSession(),
        //      user.getPrincipal().getUserName(),
        //      String.valueOf(user.getPrincipal().getProfileId()),
        //      user.getPrincipal().getRole() instanceof CAR);
        
        // Set the session timeout
        request.getSession(false).setMaxInactiveInterval(loginVo.getTimeoutInSecs());
        //request.getSession(false).setMaxInactiveInterval(60 * 60 * 5);


        request.getSession(false).setAttribute("BindingListener", 
                new SessionBindingListener(loginVo.getPrincipal(), userName));

        // Create the web trends cookie
        if ( user.getRole() instanceof ExternalClientUser )
            response.addCookie(new UserCookie(userName));
        else
            response.addCookie(new UserCookie(userName, user.getRole()));
        
    }
    
    private UserProfile createUserProfile(final LoginPSValueObject loginVo, final String keyedUserName)
    throws SystemException {
        
        final UserProfile user = new UserProfile(loginVo.getPrincipal().getUserName());
        user.setPrincipal(loginVo.getPrincipal());

        /*
         * Retrieves user preferences.
         */
        user.setPreferences(SecurityServiceDelegate.getInstance()
                .getUserPreferences(loginVo.getPrincipal()));

        if(loginVo.getNumberOfContracts() == 1 && !(user.getRole() instanceof ThirdPartyAdministrator))
        {
            // Set the contract
            // lazy load the contract dates for the current contract
            Contract contract = loginVo.getContract();
            if (contract.getContractDates()==null) 
                contract.setContractDates(EnvironmentServiceDelegate.getInstance(Constants.PS_APPLICATION_ID).getContractDates(contract.getContractNumber()));
            
            SelectContractDetailUtil.initFundsForContract(user, contract);
            user.setCurrentContract(contract);
        }

        // store the accessible contracts for message center for external user
        if (user.getRole().isExternalUser()) {
            Set<Integer> accessibleContracts = loginVo.getAccessibleContracts();
            user.setMessageCenterAccessibleContracts(accessibleContracts);
            if ( user.getRole().isTPA() ) {
                user.setMessageCenterTpaFirms(loginVo.getTpaFirms());
            }
        }
        
        // store the accessible contracts for message center for BGA CAR user
        if(user.isBundledGACAR()){
            Set<Integer> accessibleContracts = SecurityServiceDelegate.getInstance().getBGAAccessibleContracts(keyedUserName);
            user.setMessageCenterAccessibleContracts(accessibleContracts);
        }
        
        user.setNumberOfContracts(loginVo.getNumberOfContracts());
        user.setLastLoginDate(loginVo.getLastLoginDate());

        if (loginVo.getIsPasswordReset())
            user.setPasswordStatus(SecurityConstants.RESET_PASSWORD_STATUS);
        
        // set the email address into user profile
                user.setEmail(loginVo.getEmail());
                
                return user;
                
    }
        

}
