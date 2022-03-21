package com.manulife.pension.bd.web.home;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.bd.web.ApplicationHelper;
import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.content.BDContentConstants;
import com.manulife.pension.bd.web.controller.AuthorizationSubject;
import com.manulife.pension.bd.web.navigation.BDFirmLandingPages;
import com.manulife.pension.bd.web.navigation.URLConstants;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.util.CommonEnvironment;
import com.manulife.pension.service.security.BDUserRoleType;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.pension.util.content.manager.ContentCacheManager;
import com.manulife.pension.bd.web.controller.SecurityManager;

/**
 * Helper class for Home page
 * 
 * @author Ilamparithi
 * 
 */
public class HomePageHelper {

    private static Logger logger = Logger.getLogger(HomePageHelper.class);

    private static final String HOST_URL = CommonEnvironment.getInstance().getNamingVariable(
            BDConstants.HOST_URL_NAME, "bd");

    private static final String SITE_PROTOCOL = CommonEnvironment.getInstance().getNamingVariable(
            BDConstants.SITE_PROTOCOL_NAME, null);

    private static final String URL_PREFIX = SITE_PROTOCOL + "://" + HOST_URL;

    public static String MARKET_WATCH_INFO = "";

    private static HashMap<String, List<String>> SECION_ACCESS_MAP = new HashMap<String, List<String>>();

    public static final String QUICK_LINKS_SECTION = "quicklinkssection";

    public static final String MY_BOB_SECTION = "mybobsection";

    public static final String MESSAGE_CENTER_SECTION = "messagecentersection";

    public static final String BOB_SETUP_SECTION = "bobsetupsection";

    public static final String NEWS_EVENTS_SECTION = "newseventssection";

    public static final String WHATS_NEW_SECTION = "whatsnewsection";

    public static final String INVESTMENT_STORY_SECTION = "investmentstorysection";

    public static final String MARKET_COMMENTARY_SECTION = "marketcommentarysection";

	public static final String MY_PROFILE_QUICK_LINK_INTERNAL = "href= \""
			+ URLConstants.MyProfileInternal + "\"";

	public static final String MY_PROFILE_QUICK_LINK_EXTERNAL = "href= \""
			+ URLConstants.MyProfileExternal + "\"";
	
	public static final String RIA_STATEMENT_QUICK_LINK = "<li><a href=\""
			+ URLConstants.RiaEstatements + "\""
			+ ">RIA Statements"
			+ "</a></li>";
	
    static {
        try {
            Content message = ContentCacheManager.getInstance().getContentById(
                    BDContentConstants.MARKET_WATCH_CONTENT,
                    ContentTypeManager.instance().LAYOUT_PAGE);
            MARKET_WATCH_INFO = ContentUtility.getContentAttribute(message, "body3");
            if (StringUtils.isNotEmpty(MARKET_WATCH_INFO)) {
                MARKET_WATCH_INFO = StringUtils.replace(MARKET_WATCH_INFO, "{0}", URL_PREFIX);
            }
            if (logger.isDebugEnabled()) {
                logger.debug("Market Watch Info Text --> " + MARKET_WATCH_INFO);
            }
        } catch (ContentException ce) {
            logger.error("Error in retrieving CMA for Market Watch. " + ce.toString());
        } catch (Exception ex) {
            logger.error("Error in retrieving CMA for Market Watch. " + ex.toString());
        }
        SECION_ACCESS_MAP.put(BDUserRoleType.BasicFinancialRep.getRoleId(), Arrays.asList(
                QUICK_LINKS_SECTION, BOB_SETUP_SECTION, NEWS_EVENTS_SECTION, WHATS_NEW_SECTION,
                INVESTMENT_STORY_SECTION, MARKET_COMMENTARY_SECTION));
        SECION_ACCESS_MAP.put(BDUserRoleType.FinancialRep.getRoleId(), Arrays.asList(
                QUICK_LINKS_SECTION, MY_BOB_SECTION, MESSAGE_CENTER_SECTION, NEWS_EVENTS_SECTION,
                WHATS_NEW_SECTION, INVESTMENT_STORY_SECTION, MARKET_COMMENTARY_SECTION));
        SECION_ACCESS_MAP.put(BDUserRoleType.FirmRep.getRoleId(), Arrays.asList(
                QUICK_LINKS_SECTION, MY_BOB_SECTION, NEWS_EVENTS_SECTION, WHATS_NEW_SECTION,
                INVESTMENT_STORY_SECTION, MARKET_COMMENTARY_SECTION));
        SECION_ACCESS_MAP.put(BDUserRoleType.FinancialRepAssistant.getRoleId(), Arrays.asList(
                QUICK_LINKS_SECTION, MY_BOB_SECTION, MESSAGE_CENTER_SECTION, NEWS_EVENTS_SECTION,
                WHATS_NEW_SECTION, INVESTMENT_STORY_SECTION, MARKET_COMMENTARY_SECTION));
        SECION_ACCESS_MAP.put(BDUserRoleType.RVP.getRoleId(), Arrays.asList(QUICK_LINKS_SECTION,
                MY_BOB_SECTION, NEWS_EVENTS_SECTION, WHATS_NEW_SECTION, INVESTMENT_STORY_SECTION,
                MARKET_COMMENTARY_SECTION));
        SECION_ACCESS_MAP.put(BDUserRoleType.CAR.getRoleId(), Arrays.asList(QUICK_LINKS_SECTION,
                NEWS_EVENTS_SECTION, WHATS_NEW_SECTION, INVESTMENT_STORY_SECTION,
                MARKET_COMMENTARY_SECTION));
        SECION_ACCESS_MAP.put(BDUserRoleType.SuperCAR.getRoleId(), Arrays.asList(QUICK_LINKS_SECTION,
                NEWS_EVENTS_SECTION, WHATS_NEW_SECTION, INVESTMENT_STORY_SECTION,
                MARKET_COMMENTARY_SECTION));
        SECION_ACCESS_MAP.put(BDUserRoleType.InternalBasic.getRoleId(), Arrays.asList(QUICK_LINKS_SECTION,
                NEWS_EVENTS_SECTION, WHATS_NEW_SECTION, INVESTMENT_STORY_SECTION,
                MARKET_COMMENTARY_SECTION));
        SECION_ACCESS_MAP.put(BDUserRoleType.Administrator.getRoleId(), Arrays.asList(QUICK_LINKS_SECTION,
                NEWS_EVENTS_SECTION, WHATS_NEW_SECTION, INVESTMENT_STORY_SECTION,
                MARKET_COMMENTARY_SECTION));
        SECION_ACCESS_MAP.put(BDUserRoleType.NationalAccounts.getRoleId(), Arrays.asList(QUICK_LINKS_SECTION,
                NEWS_EVENTS_SECTION, WHATS_NEW_SECTION, INVESTMENT_STORY_SECTION,
                MARKET_COMMENTARY_SECTION));
        SECION_ACCESS_MAP.put(BDUserRoleType.ContentManager.getRoleId(), Arrays.asList(QUICK_LINKS_SECTION,
                NEWS_EVENTS_SECTION, WHATS_NEW_SECTION, INVESTMENT_STORY_SECTION,
                MARKET_COMMENTARY_SECTION));
        SECION_ACCESS_MAP.put(BDUserRoleType.RIAUser.getRoleId(), Arrays.asList(
                QUICK_LINKS_SECTION,WHATS_NEW_SECTION,
                INVESTMENT_STORY_SECTION, MARKET_COMMENTARY_SECTION));
    }

    /**
     * Return a flat to indicate whether a particular section is allowed for the given a user or
     * not.
     * 
     * @param userRoleId
     * @param section
     * @return boolean
     */
    public static boolean isSectionAllowed(String userRoleId, String section) {
        List<String> allowedSections = SECION_ACCESS_MAP.get(userRoleId);
        if (allowedSections == null) {
        	return false;
        } else {
        	return (allowedSections.contains(section));
        }
    }

    /**
     * Get the HTML string for quick link My profile
     * @param request
     * @param application
     * @return
     * @throws SystemException
     */
	public static String getQuickLinkMyProfileHtml(ServletRequest request,
			ServletContext application) throws SystemException {
		HttpServletRequest req = (HttpServletRequest) request;
		BDUserProfile profile = BDSessionHelper
				.getUserProfile((HttpServletRequest) req);
		if (profile.isInternalUser()) {
			return MY_PROFILE_QUICK_LINK_INTERNAL;
		} else {
			AuthorizationSubject subject = ApplicationHelper
					.getAuthorizationSubject(req);
			SecurityManager securityManager = ApplicationHelper
					.getSecurityManager(application);
			if (securityManager.isUserAuthorized(subject,
					URLConstants.MyProfileExternal)) {
				return MY_PROFILE_QUICK_LINK_EXTERNAL;
			} else {
				return "";
			}
		}
	}
	
    /**
     * Get the HTML string for quick link for RUM/RIA
     * @param request
     * @param application
     * @return
     * @throws SystemException
     */
	public static String getQuickLinkforRUM(ServletRequest request,
			ServletContext application) throws SystemException {
		HttpServletRequest req = (HttpServletRequest) request;
		AuthorizationSubject subject = ApplicationHelper
				.getAuthorizationSubject(req);
		SecurityManager securityManager = ApplicationHelper
				.getSecurityManager(application);
		if (securityManager.isUserAuthorized(subject,
				URLConstants.RiaEstatements)) {
			return RIA_STATEMENT_QUICK_LINK;
		} else {
			return "";
		}
	}
	
	/**
	 * Get the HTML string for quick link for Fiduciary Resources  
	 * @param request
	 * @param application
	 * @return
	 * @throws SystemException
	 */
	public static String getQuickLinkforFiduciary(ServletRequest request,
			ServletContext application) throws SystemException {
		HttpServletRequest req = (HttpServletRequest) request;
		String approvingFirmCode = BDConstants.BLANK;
		int contentId = BDConstants.NUM_MINUS_1;
		String keyStr = "";
		int key = 0;
		BDUserProfile bdUserProfile = BDSessionHelper.getUserProfile(req);
		if (!bdUserProfile.isInternalUser()) {
			approvingFirmCode = bdUserProfile.getAssociatedApprovingFirmCode();
			contentId = BDFirmLandingPages.getInstance()
					.getPartneringLandingPageCMAKey(approvingFirmCode);
			if (contentId == 66310) {
				key = 81919;
			} else if (contentId == 66316) {
				key = 81919;
			} else {
				key = 81920;
			}
		} else {
			key = 81919;
		}
		keyStr = "/do/content?id=" + StringUtils.trimToEmpty(String.valueOf(key));
		return keyStr;
	}

	/**
	 * This method returns the cookie value from the array of Cookies in the request
	 * with a particular name. If the cookie is not found, it returns a default value 
	 *  - empty string
	 * @param cookies
	 * @param cookieName
	 * @param defaultValue
	 * @return
	 */
	public static String getCookieValue(Cookie[] cookies, String cookieName) {
		String defaultValue = StringUtils.EMPTY;
		if(ArrayUtils.isEmpty(cookies)) {
			return defaultValue;
		}
		for(int i=0; i<cookies.length; i++) {
		Cookie cookie = cookies[i];
		if (cookieName.equals(cookie.getName()))
			return(cookie.getValue());
		}
		return(defaultValue);
	}
}
