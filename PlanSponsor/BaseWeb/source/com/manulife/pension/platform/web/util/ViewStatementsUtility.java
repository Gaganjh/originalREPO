package com.manulife.pension.platform.web.util;

import javax.servlet.http.Cookie;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;

/**
 * ViewStatementsUtility class some static methods to access the Data Impact site for viewing
 * statements
 * 
 * @author AAmbrose
 * 
 */
public class ViewStatementsUtility {
    private static final Logger logger = Logger.getLogger(ViewStatementsUtility.class);

    private ViewStatementsUtility() {
    }

    /**
     * Generate an encrypted cookie for the Data Impact site
     * 
     * @param int contractNumber
     * @param String ssn
     * @return Cookie cookie
     */
    public static Cookie getCookie(int contractNumber, String ssn) {

        String strCookie = null;

        try {
            strCookie = ContractServiceDelegate.getInstance().getStatementsCookie(contractNumber,
                    ssn);
        } catch (SystemException se) {
            logger.error("SystemException caught in ViewStatementsUtility.getCookie() method.");
            // LogUtility.logSystemException(Constants.PS_APPLICATION_ID,se);
        }

        Cookie cookie = new Cookie("rep_login", strCookie);
        cookie.setComment("This cookie is required to view statements.");
        cookie.setMaxAge(1200);

        String domain = CommonEnvironment.getInstance().getCookieDomain();
        if (domain == null) {
            domain = "";
        }
        if (!domain.equals("")) {
            cookie.setDomain(domain);
        }
        cookie.setPath("/");
        
        // Checks if current site protocol is https then sets Secure flag as true
        String siteProtocal = CommonEnvironment.getInstance().getSiteProtocol();
        if (StringUtils.contains(siteProtocal, CommonConstants.HTTPS)) {
        	cookie.setSecure(true);
        }else{
        	cookie.setSecure(false);
        }
        return cookie;
    }

    /**
     * Getnerate URL for the Data Impact site
     * 
     * @param String type
     * @return String url
     */
    public static String getUrlForStatements(String type) {
        return CommonEnvironment.getInstance().getStatementsURL() + "&path=client&output=" + type;
    }

    /**
     * Getnerate URL for the Data Impact site (for participants)
     * 
     * @param String type
     * @return String url
     */
    public static String getUrlForParticipantStatements(String type) {
        return CommonEnvironment.getInstance().getStatementsURL() + "&path=participant&output="
                + type;
    }

}
