package com.manulife.pension.bd.web.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.manulife.pension.bd.web.ApplicationHelper;
import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.tracking.BDLogoutTracker;
import com.manulife.pension.bd.web.userprofile.BDAssistantUserProfile;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.userprofile.BDUserProfileFactory;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.passcode.AuthenticatedSessionInitialization;
import com.manulife.pension.platform.web.util.BaseSessionHelper;
import com.manulife.pension.service.security.bd.valueobject.BDLoginValueObject;

enum BdAuthenticatedSessionInitialization
implements AuthenticatedSessionInitialization<BDLoginValueObject> {
    
    INSTANCE;
    
    @Override
    public void execute(
            final String userName,
            final HttpServletRequest request,
            final HttpServletResponse response,
            final BDLoginValueObject lvo)
    throws SystemException { 
        
        BDUserProfile userProfile = BDUserProfileFactory.getInstance().getUserProfile(lvo.getPrincipal());
        // set the password status
        userProfile.setPasswordStatus(lvo.getPasswordStatus());
        userProfile.setDefaultFundListing(lvo.getFundDefaultSite());
        if (userProfile instanceof BDAssistantUserProfile) {
            ((BDAssistantUserProfile) userProfile).setParentPrincipal(lvo.getParentPrincipal());
        }
        // Invalidate the session
        BaseSessionHelper.invalidateSessionKeepCookie(request);
        ApplicationHelper.setUserProfile(request, userProfile);
        
        request.getSession(false).setAttribute(BDConstants.BD_LOGIN_VALUE_OBJECT, lvo);
        // set the timeout in secs for the session
        request.getSession(false).setMaxInactiveInterval(lvo.getTimeoutInSecs());
        
        request.getSession(false).setAttribute(BDConstants.BD_LOGIN_TRACK, new BDLogoutTracker(userProfile.getBDPrincipal()));
        //request.getSession(false).setAttribute(BDConstants.DIRECT_URL_ATTR,directUrl);
        try {
            UserCookie webtrendsCookie = new UserCookie(userProfile);
            response.addCookie(webtrendsCookie);
        } catch (Exception f) {
        }
        
    }      
    
}