package com.manulife.pension.bd.web.login;

import javax.servlet.http.Cookie;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.platform.web.util.CommonEnvironment;

/**
 * @author Mark Eldridge
 * @date September 2009
 * 
 *       This cookie holds the WebTrends Information. The NFA parameter is used by the Find Literature page.
 * 
 *       The cookie is set to expire immediately on session closing because this data should not be persisted as a
 *       cookie to the users hard drive. It will remain as long as the browser is open - which is all we need it for.
 * 
 *       NOTE: The order and of the parameters is very important to WebTrends as there are regular expressions that are
 *       being used which may expect order to be maintained. If you need to add a parameter to the cookie, I recommend
 *       adding it to the end of the cookie string, and following the pattern of always ensuring the key always has an
 *       associated value, but doing proper error trapping, and null/"" checking.
 * 
 */
public class UserCookie extends Cookie {
	
	private static String SITE_PROTOCOL = CommonEnvironment.getInstance().getSiteProtocol();

    public static final String NAME = "BDUser";

    private String profileId = "0";

    private String firmId = "0";

    private String userRole = "UNKNWN";

    private String nfaFirmId = "0";

    /**
     * Creates/Resets a WebTrends tracking cookie for the user, when logging in or exiting Mimic mode.
     * 
     * @param currentUser - The user profile of the current user.
     */
    public UserCookie(BDUserProfile currentUser) {
        // Null is passed to the other contructor to indicate we are not mimicking anyone.
        this(currentUser, null);
    }

    /**
     * Creates/Resets a WebTrends tracking cookie for the user. If the user is in mimick mode then a user profile of the
     * mimicked person will be passed in. This is to set the NFA code - which must reflect the mimicked user. All the
     * other data has to remain the same, as WebTrends wants to track that individual not the mimicked user. NFA code is
     * NOT one of the webtrends tracked values. It is used by Find Literature. We're just sharing a cookie.
     * 
     * @param currentUser - The actual user
     * @param mimickedProfile - The mimicked user or NULL if not mimicking a user.
     */
    public UserCookie(BDUserProfile currentUser, BDUserProfile mimickedProfile) {
        super(NAME, null);

        // Nothing we do here should prevent people from logging in. Capture as much data as possible
        // And create the cookie with whatever we can get.
        try {
            profileId = new Long(currentUser.getBDPrincipal().getProfileId()).toString();
        } catch (Exception e) {
        }
        try {
            firmId = currentUser.getRole().getGoverningBDFirmPartyId().toString();
        } catch (Exception e) {
        }
        try {
            userRole = currentUser.getRole().getRoleType().getUserRoleCode();
        } catch (Exception e) {
        }
        try {
            if (mimickedProfile == null) {
                nfaFirmId = currentUser.getAssociatedApprovingFirmCode();
            } else {
                nfaFirmId = mimickedProfile.getAssociatedApprovingFirmCode();
            }
        } catch (Exception e) {
        }

        // No matter what happens above, we need a value for each parameter.
        // So we double check for nulls and empty strings.
        if (profileId == null || "".equals(profileId)) {
            profileId = "0";
        }
        if (firmId == null || "".equals(firmId)) {
            firmId = "0";
        }
        if (userRole == null || "".equals(userRole)) {
            userRole = "UNKNWN";
        }
        if (nfaFirmId == null || "".equals(nfaFirmId)) {
            nfaFirmId = "0";
        }
        // The following cookie format is required for webtrends to be able to read the data in the cookie.
        // PID,FID,UR,NFA
        String webTrendsData = "PID_" + profileId + ",FID_" + firmId + ",UR_" + userRole + ",NFA_" + nfaFirmId;
        this.setValue(webTrendsData);
        this.setComment("WebTrends Cookie");
        this.setMaxAge(-1); // Cookie is not to be stored persistently.
        this.setPath("/");
        
        // Checks if current site protocol is https then sets Secure flag as true
        if (StringUtils.contains(SITE_PROTOCOL, BDConstants.HTTPS)) {
        	this.setSecure(true);
        }else{
        	this.setSecure(false);
        }
    }
}
